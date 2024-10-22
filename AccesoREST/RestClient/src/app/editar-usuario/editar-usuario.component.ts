import { Component } from '@angular/core';
import { Usuario } from '../shared/app.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { Observable } from 'rxjs';
import { DataService } from '../shared/data.service';

@Component({
  selector: 'app-editar-usuario',
  templateUrl: './editar-usuario.component.html',
  styleUrls: ['./editar-usuario.component.css']
})
export class EditarUsuarioComponent {

  usuario! : Usuario;

  id!: Number;
  operacion!:String;

  constructor(private ruta:ActivatedRoute, private router:Router, private clienteApiRest:ClienteApiRestService, private datos:DataService) {}

  errorMensaje : string = '';

  ngOnInit() {
    // Operacion: va en el ultimo string (parte) de la URL
    this.operacion = this.ruta.snapshot.url[this.ruta.snapshot.url.length - 1].path;

      console.log("En Editar");
      this.ruta.paramMap.subscribe( // Capturamos el id de la URL
      params => {
        this.id = Number(params.get('id')!); // Se usa "!" para evitar error en compilacion.
        // No va a ser null nunca
      },
        err => console.log("Error al leer id para editar: " + err)
      )
      // console.log("Id: " + this.id);
      this.clienteApiRest.getUsuario(this.id).subscribe( // Leemos de la base de datos vía API
        resp => {
          this.usuario = resp.body!;
        },
        err => {
          console.log("Error al traer el vino: " + err.message);
          throw err;
        }
      )

  }

  onSubmit() {
    console.log("Enviado formulario");
    if (this.id) { // si existe id estamos en edicion

      this.clienteApiRest.modificarUsuario(String(this.usuario.id), this.usuario).subscribe(
        resp => {
        if (resp.status < 400) { // Si no hay error en la operacion por parte del servicio
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje(resp.body); // Mostramos el mensaje enviado por el API
        } else {
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje("Error al modificar el usuario");
        }
          this.router.navigate(['users']); // Volvemos a la vista 1 (listado de usuarios)
        },
        err=> {
          console.log("Error al editar: " + err.message);
          this.errorMensaje = err.error;
          throw err;
        }
      )

    }
  }

}
