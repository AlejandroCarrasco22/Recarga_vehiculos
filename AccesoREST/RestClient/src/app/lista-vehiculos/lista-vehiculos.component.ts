import { Component, OnInit } from '@angular/core';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { Vehiculo } from '../shared/app.model';
import { DataService } from '../shared/data.service';


@Component({
  selector: 'app-lista-vehiculos',
  templateUrl: './lista-vehiculos.component.html',
  styleUrls: ['./lista-vehiculos.component.css']
})
export class ListaVehiculosComponent implements OnInit {
  Vehiculos!: Vehiculo[];
  mostrarMensaje!: boolean;
  mensaje!: string;
  usuarioId: String = "";

  // Inyectamos los servicios
  userId!: string;

  constructor( private clienteApiRest: ClienteApiRestService, private datos: DataService)
  { }


  ngOnInit() {

    /*** Cargamos vehiculos accediendo a la respuesta */
    this.getVehiculos();
  }

  onSubmit(){
    // Método para obtener los vehículos de un usuario cuyo id se ha introducido
    // en el campo correspondiente.
    this.clienteApiRest.getVehiculosPorUsuario(this.userId).subscribe(
        resp =>{
          if (resp.status < 400 ) {
            this.Vehiculos = resp.body!;
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



    getVehiculos() {
      this.clienteApiRest.getAllVehiculos().subscribe(
        resp =>{
          if (resp.status < 400 ) { // Si no hay error en la respuesta
            this.Vehiculos = resp.body!; // se accede al cuerpo de la respuesta
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

    borrarVehiculo(id: Number) {
      this.clienteApiRest.borrarVehiculo(String(id)).subscribe(
        resp => {
          if (resp.status < 400) { // Si no hay error en la respuesta
            // actualizamos variable compartida
            this.mostrarMensaje = true;
            // actualizamos variable compartida
            this.mensaje = resp.body; // mostramos el mensaje retornado por el API
            //Actualizamos la lista de vinos en la vista
            this.getVehiculos();
          } else {
            this.mostrarMensaje = true;
            this.mensaje = "Error al eliminar registro";
          }
        },
        err=> {
          console.log("Error al borrar: " + err.message);
          throw err;
        }
      )
    }

    getVehiculosPorUsuarioId() {
      this.clienteApiRest.getVehiculoByIdUsuario(this.usuarioId).subscribe(
        resp => {
          if (resp.status < 400) {
            this.Vehiculos = resp.body!;
            console.log(this.Vehiculos);
          } else {
            this.mensaje = 'Error al acceder a los datos';
            this.mostrarMensaje = true;
          }
        },
        err => {
          console.log("Error al traer la lista: " + err.message);
          throw err;
        }
      );
    }

}
