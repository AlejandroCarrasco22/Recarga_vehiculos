import { Component } from '@angular/core';
import {Usuario} from "../shared/app.model";
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../shared/data.service';


@Component({
  selector: 'app-anadir-usuario',
  templateUrl: './anadir-usuario.component.html',
  styleUrls: ['./anadir-usuario.component.css']
})
export class AnadirUsuarioComponent {

  usuarioVacio = {
    id: 0,
    name: "",
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    paymentCard: "",
    enabled: false,
    vehiculoCollection: []
  }

  usuario = this.usuarioVacio as Usuario;


  constructor(private ruta: ActivatedRoute, private router: Router,
              private clienteApiRest: ClienteApiRestService, private datos: DataService)
  { }

  errorMensaje : string = '';


  onSubmit(){
    console.log("Formulario de nuevo usuario enviado.");
    this.clienteApiRest.anadirUsuario(this.usuario).subscribe(
      resp => {
        // En caso de ser correcta la petición, creamos un usuario nuevo con los datos
        // introducidos en el formulario, mostrando el mensaje correspondiente.
        if(resp.status < 400){
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje(resp.body);
        }
        else{
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje("Error al añadir un nuevo usuario.");
        }
        this.router.navigate(['users']);
      },
      err => {
        console.log("Error al crear un usuario: "+ err.message);
        this.errorMensaje = err.error;
        throw err;
      }
    )
  }

}
