import { Component } from '@angular/core';
import { PlugType, PuntoDeCarga } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';

@Component({
  selector: 'app-lista-puntos-carga',
  templateUrl: './lista-puntos-carga.component.html',
  styleUrls: ['./lista-puntos-carga.component.css']
})
export class ListaPuntosCargaComponent {
  Puntos!: PuntoDeCarga[];
  mostrarMensaje!: boolean;
  mensaje!: string;


  // Inyectamos los servicios
  vehicleId!: string;
  constructor( private clienteApiRest: ClienteApiRestService, private datos: DataService)
  { }


  //método ejecutado tras la construcción del componente. Es el lugar adecuado para cargar datos

  ngOnInit() {
    /*** Cargamos vehiculos accediendo a la respuesta */
    this.getPuntosDeCarga();
  }

  onSubmit(){
    // Método para obtener los vehículos de un usuario cuyo id se ha introducido
    // en el campo correspondiente.
    this.clienteApiRest.getPuntosRecargaPorPlugType(this.vehicleId).subscribe(
      resp =>{
        if (resp.status < 400 ) {
          this.Puntos = resp.body!;
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

    getPuntosDeCarga() {
      this.clienteApiRest.getPuntosCarga().subscribe(
        resp =>{
          if (resp.status < 400 ) { // Si no hay error en la respuesta
            this.Puntos = resp.body!; // se accede al cuerpo de la respuesta
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
