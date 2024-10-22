import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {PlugType, Usuario, Vehiculo} from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';
import {HttpResponse} from "@angular/common/http";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-anadir-vehiculo',
  templateUrl: './anadir-vehiculo.component.html',
  styleUrls: ['./anadir-vehiculo.component.css']
})
export class AnadirVehiculoComponent {

  vehiculoVacio = {
    id: 0,
    carRegistration: "",
    userId: 0,
    brand: "",
    model: "",
    capacity: 0,
    plugType: ""
  }

  vehiculo = this.vehiculoVacio as Vehiculo;

  // Obtenemos idUsuario del campo correspondiente del formulario de añadir usuario.
  idUsuario = new FormControl('')


  constructor(private ruta: ActivatedRoute, private router: Router,
              private clienteApiRest: ClienteApiRestService, private datos: DataService){}

  errorMensaje : string = '';

  onSubmit(){
    console.log("Formulario de nuevo vehículo enviado.");

    // value devuelve el valor que se ha introducido en el campo del id de usuario.
    this.clienteApiRest.getUsuario(Number(this.idUsuario.value)).subscribe(
      // Buscamos al usuario correspondiente al id aportado en el formulario, y en el caso de existir,
      // añadimos el vehículo a la BD.
      (response: HttpResponse<Usuario>) => {
        // Si la petición tiene lugar sin errores y el id aportado corresponde a un usuario existente en la BD
        // (response.body no está vacío), procedemos a llamar al método encargado de añadir el vehículo a la BD.
        if(response.status == 200 && response.body){
          //this.vehiculo.userId = response.body;
          this.vehiculo.userId = response.body.id;

          // Método para añadir un nuevo vehículo a la BD, basado en los datos aportados en el formulario.
          this.clienteApiRest.anadirVehiculo(this.vehiculo).subscribe(
            resp => {
              // En caso de ser correcta la petición, creamos un vehiculo nuevo con los datos
              // introducidos en el formulario, mostrando el mensaje correspondiente.
              if(resp.status < 400){
                this.datos.cambiarMostrarMensaje(true);
                this.datos.cambiarMensaje(resp.body);
              }
              else{
                this.datos.cambiarMostrarMensaje(true);
                this.datos.cambiarMensaje("Error al añadir un nuevo vehiculo.");
              }
              // Cuando se acabe de añadir el Vehículo, volvemos a la lista de estos.
              this.router.navigate(['vehicles']);
            },
            err => {
              console.log("Error al crear un vehículo nuevo: "+ err.message)
              this.errorMensaje = err.error;
              throw err;
            }
          )

        }
        else{
          console.error("El usuario con el ID especificado no existe o ha habido un error en la solicitud.")
        }
      },
      err => {
        console.error('Error al buscar el usuario:', err.message);
        this.errorMensaje = err.error;
        throw err;
      }

    )

  }


}
