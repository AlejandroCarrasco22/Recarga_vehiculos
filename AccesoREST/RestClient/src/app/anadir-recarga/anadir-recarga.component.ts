import { Component } from '@angular/core';
import {Recarga} from "../shared/app.model";
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../shared/data.service';

@Component({
  selector: 'app-anadir-recarga',
  templateUrl: './anadir-recarga.component.html',
  styleUrls: ['./anadir-recarga.component.css']
})
export class AnadirRecargaComponent {

    recargaVacia = {
      id : 0,
      userId : 0,
      vehicleId : 0,
      chargerpointId : 0,
      price : 0,
      kw : 0,
      status : "NOTSTARTED",
      payment: "NOTPROCESSED"
    }

    recarga = this.recargaVacia as Recarga;

    constructor(private ruta: ActivatedRoute, private router: Router,
                  private clienteApiRest: ClienteApiRestService, private datos: DataService)
      { }

    errorMensaje: string = '';

    onSubmit(){
      console.log("Formulario de nueva recarga enviado.");
          this.clienteApiRest.anadirRecarga(this.recarga).subscribe(
            resp => {
              // En caso de ser correcta la petición, creamos una recarga nueva con los datos
              // introducidos en el formulario, mostrando el mensaje correspondiente.
              if(resp.status < 400){
                this.datos.cambiarMostrarMensaje(true);
                this.datos.cambiarMensaje(resp.body);
              }
              else{
                this.datos.cambiarMostrarMensaje(true);
                this.datos.cambiarMensaje("Error al añadir una nueva recarga.");
              }
              this.router.navigate(['recharge']);
            },
            err => {
              console.log("Error al crear: "+ err.message)
              this.errorMensaje = err.error;
              throw err;
            }
          )
    }
}
