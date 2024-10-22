import { Component } from '@angular/core';
import { PuntoDeCarga } from '../shared/app.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';


@Component({
  selector: 'app-editar-puntos-carga',
  templateUrl: './editar-puntos-carga.component.html',
  styleUrls: ['./editar-puntos-carga.component.css']
})

export class EditarPuntosCargaComponent {

  puntoCargaVacio = {
    id : 0,
    address : "",
    latitude : 0,
    longitude : 0,
    plugType : "",
    power : "",
    status : ""
  }

  puntoCarga = this.puntoCargaVacio as PuntoDeCarga;

  id! : String;
  operacion! : String;

  constructor(private ruta: ActivatedRoute, private router: Router,
              private clienteApiRest: ClienteApiRestService, private datos: DataService)
  { }

  errorMensaje : string = '';


  ngOnInit() {
    console.log("En editar-puntocarga");
    // Operacion: va en el ultimo string (parte) de la URL
    this.operacion = this.ruta.snapshot.url[this.ruta.snapshot.url.length - 1].path;

      console.log("En Editar");
      this.ruta.paramMap.subscribe( // Capturamos el id de la URL
      params => {
        this.id = params.get('id')!; // Se usa "!" para evitar error en compilacion.
        // No va a ser null nunca
      },
        err => console.log("Error al leer id para editar: " + err)
      )
      // console.log("Id: " + this.id);
      this.clienteApiRest.getPuntoCarga(this.id).subscribe( // Leemos de la base de datos vía API
        resp => {
          this.puntoCarga = resp.body!; // No comprobamos “status”. El vino que existe seguro
          // Se usa “!” por la misma razón que antes
        },
        err => {
          console.log("Error al traer el punto de carga: " + err.message);
          throw err;
        }
      )

  }

  onSubmit() {
    console.log("Formulario enviado");

    if (this.id) { // Comprobamos que exista la id del punto de carga.
      this.clienteApiRest.modificarPuntoCarga(String(this.puntoCarga.id), this.puntoCarga).subscribe(
        resp => {
          if (resp.status < 400) { // Si no hay error en la operacion por parte del servicio
            this.datos.cambiarMostrarMensaje(true);
            this.datos.cambiarMensaje(resp.body);
          } else {
            this.datos.cambiarMostrarMensaje(true);
            this.datos.cambiarMensaje("Error al modificar el punto de recarga");
          }

          this.router.navigate(['chargerpoints']); // Volvemos a la vista 1 (listado de usuarios)
        },
        err=> {
          console.log("Error al editar el punto de carga: " + err.message);
          this.errorMensaje = err.error;
          throw err;
        }
      )

    }
  }


}
