import { Component } from '@angular/core';
import { Recarga } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-recarga-detallada',
  templateUrl: './recarga-detallada.component.html',
  styleUrls: ['./recarga-detallada.component.css']
})
export class RecargaDetalladaComponent {

  Recarga! : Recarga;
  id! : Number;
  mostrarMensaje!: boolean;
  mensaje!: string;

  constructor(private ruta: ActivatedRoute, private clienteApiRest: ClienteApiRestService, private datos: DataService){}

  ngOnInit(){
  // Cargamos las recargas del usuario
    this.getRecargaDetallada();
  }

  getRecargaDetallada(){
    this.ruta.paramMap.subscribe( // Capturamos el id de la URL
      params => {
        this.id = Number(params.get('id')!); // Se usa "!" para evitar error en compilacion.
        // No va a ser null nunca
      },
      err => console.log("Error al leer id para editar: " + err)
    )

    this.clienteApiRest.getRecargaPorId(this.id).subscribe(
      resp => {
      if (resp.status < 400 ) {
          this.Recarga = resp.body!;
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

