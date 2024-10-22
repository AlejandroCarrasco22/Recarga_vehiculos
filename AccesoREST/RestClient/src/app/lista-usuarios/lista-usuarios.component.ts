import { Component } from '@angular/core';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { Usuario } from '../shared/app.model';
import { DataService } from '../shared/data.service';

@Component({
  selector: 'app-lista-usuarios',
  templateUrl: './lista-usuarios.component.html',
  styleUrls: ['./lista-usuarios.component.css']
})
export class ListaUsuariosComponent {

  Usuarios!: Usuario[];
  mostrarMensaje!: boolean;
  mensaje!: string;
  filtroSeleccionado: String = "Todos";


  // Inyectamos los servicios
  constructor( private clienteApiRest: ClienteApiRestService, private datos: DataService)
  { }
  
  
  //método ejecutado tras la construcción del componente. Es el lugar adecuado para cargar datos
  
  ngOnInit() {
    
    /*** Cargamos usuarios accediendo a la respuesta */
    this.getUsuarios();
    }
    
    
    
    getUsuarios() {
      this.filtroSeleccionado = "Todos";
      this.clienteApiRest.getAllUsuarios().subscribe(
        resp =>{
          if (resp.status < 400 ) { // Si no hay error en la respuesta
            this.Usuarios = resp.body!; // se accede al cuerpo de la respuesta
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

    getUsuariosActivados(){
      this.filtroSeleccionado = "Activado";
      this.clienteApiRest.getUsuariosEnabled().subscribe(
        resp =>{
          if (resp.status < 400 ) { // Si no hay error en la respuesta
            this.Usuarios = resp.body!; // se accede al cuerpo de la respuesta
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


    getUsuariosDesactivados(){
      this.filtroSeleccionado = "Desactivado";
      this.clienteApiRest.getUsuariosDisabled().subscribe(
        resp =>{
          if (resp.status < 400 ) { // Si no hay error en la respuesta
            this.Usuarios = resp.body!; // se accede al cuerpo de la respuesta
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


    borrarUsuario(id: Number) {
      this.clienteApiRest.borrarUsuario(String(id)).subscribe(
        resp => {
        if (resp.status < 400) { // Si no hay error en la respuesta
          // actualizamos variable compartida
          this.mostrarMensaje = true;
          // actualizamos variable compartida
          this.mensaje = resp.body; // mostramos el mensaje retornado por el API
          //Actualizamos la lista de vinos en la vista
          this.getUsuarios();
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
}
