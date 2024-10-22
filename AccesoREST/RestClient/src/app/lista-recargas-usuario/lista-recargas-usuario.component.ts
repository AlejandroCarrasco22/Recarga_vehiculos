import { Component } from '@angular/core';
import { Recarga } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';

@Component({
  selector: 'app-lista-recargas-usuario',
  templateUrl: './lista-recargas-usuario.component.html',
  styleUrls: ['./lista-recargas-usuario.component.css']
})
export class ListaRecargasUsuarioComponent {
  RecargasUsuario! : Recarga[];
  mostrarMensaje!: boolean;
  mensaje!: string;

  userId! : Number;

  constructor( private clienteApiRest: ClienteApiRestService, private datos: DataService)
  { }

  errorMensaje : string = '';


  onSubmit(){
    this.getRecargasDeUsuario(this.userId);
  }

  // En el caso de que se pulse el botón de empezar una recarga.
  accionStart(id:Number, userId:Number){
    this.clienteApiRest.modificarRecarga(id, "START").subscribe(
      resp =>{
        if (resp.status < 400 ) {
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje(resp.body);
        } else {
          this.mensaje = 'Error al modificar la recarga.';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al modificar el estado de la recarga: " + err.message);
        this.errorMensaje = err.error;
        throw err;
      }
    )
    // Después de haber actualizado el estado de la propia recarga y del pago,
    // actualizamos la lista de recargas para poder observar el cambio.
    this.getRecargasDeUsuario(userId);
  }

  // En el caso de que se cancele una recarga.
  accionCancel(id:Number, userId:Number){
    this.clienteApiRest.modificarRecarga(id, "CANCEL").subscribe(
      resp =>{
        if (resp.status < 400 ) {
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje(resp.body);
        } else {
          this.mensaje = 'Error al modificar la recarga.';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al modificar el estado de la recarga: " + err.message);
        this.errorMensaje = err.error;
        throw err;
      }
    )
    this.getRecargasDeUsuario(userId);
  }

  // En el caso de que se finalice una recarga en curso.
  accionFinalizar(id:Number, userId:Number){
    this.clienteApiRest.modificarRecarga(id, "STOP").subscribe(
      resp =>{
        if (resp.status < 400 ) {
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje(resp.body);
        } else {
          this.mensaje = 'Error al modificar la recarga.';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al modificar el estado de la recarga: " + err.message);
        this.errorMensaje = err.error;
        throw err;
      }
    )
    this.getRecargasDeUsuario(userId);
  }

  // Muestra la lista de recargas de un usuario concreto en pantalla.
  getRecargasDeUsuario(userId:Number){
    this.clienteApiRest.getRecargasDeUsuario(userId).subscribe(
      resp =>{
        if (resp.status < 400 ) {
          this.RecargasUsuario = resp.body!;
        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
  }


}
