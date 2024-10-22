import {Component, ElementRef, NgZone, ViewChild} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PuntoDeCarga } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';
import {latLng, Map, tileLayer, Marker, LeafletMouseEvent, LayerGroup, LeafletEvent, LatLng} from 'leaflet';

@Component({
  selector: 'app-anadir-punto-carga',
  templateUrl: './anadir-punto-carga.component.html',
  styleUrls: ['./anadir-punto-carga.component.css']
})
export class AnadirPuntoCargaComponent {
  //@ViewChild('map') mapElement!: ElementRef;

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
  map!: Map | LayerGroup<any>;
  marker!: Marker ;


  constructor(private ngZone: NgZone, private ruta: ActivatedRoute, private router: Router,
              private clienteApiRest: ClienteApiRestService, private datos: DataService)
  { }

  errorMensaje : string = '';

  ngOnInit(): void {
    this.ngZone.runOutsideAngular(() => {
      this.inicializarMapa();
    }); 
  }

  inicializarMapa(): void {
    // Vista inicial del mapa
    this.map = new Map('map').setView([41.65518, -4.72372], 12);

    // Añade una capa de mapa (por ejemplo, OpenStreetMap)
    tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    // Sitúa el marcador inicial.
    this.marker = new Marker([41.65518, -4.72372], { draggable: true }).addTo(this.map);

    // Sitúa el marcador en el punto del mapa que se haya pulsado.
    this.map.on('click', (event: LeafletMouseEvent) => {
      this.actualizarMapa(event.latlng.lat, event.latlng.lng);
    });

    // Actualiza el marcador con la latitud y longitud actuales.
    // if (this.puntoCarga.latitude && this.puntoCarga.longitude) {
    // this.actualizarMapa(this.puntoCarga.latitude, this.puntoCarga.longitude);
    // }
  }

  actualizarMapa(latitude: number, longitude: number): void {
    this.ngZone.runOutsideAngular(() => {
      // ELimina el marcador en caso de haber uno.
      if (this.marker) {
        this.map?.removeLayer(this.marker);
      }

      // Añade un marcador nuevo
      this.marker = new Marker([latitude, longitude], {
        draggable: true
      }).addTo(this.map);

      // Actualiza los campos de longitud y latitud en función de donde se haya situado el marcador.
      this.puntoCarga.latitude = latitude;
      this.puntoCarga.longitude = longitude;

      // Actualiza los campos del formulario si fuera necesario.
      // this.puntoCargaForm.controls['latitude'].setValue(latitude);
      // this.puntoCargaForm.controls['longitude'].setValue(longitude);

      // Escucha a eventos de arrastre del marcador en el mapa.
      this.marker.on('dragend', (event: LeafletEvent) => {
        const posicion: LatLng = this.marker.getLatLng();
        this.actualizarMapa(posicion.lat, posicion.lng);
      });

      // Escucha a eventos de zoom.
      this.map.on('zoomend', () => {
        const posicion: LatLng = this.marker.getLatLng();
        this.actualizarMapa(posicion.lat, posicion.lng);
      });
    });
  }

  onSubmit(){
    console.log("Formulario de nuevo usuario enviado.");
    this.clienteApiRest.anadirPuntoCarga(this.puntoCarga).subscribe(
      resp => {
        // En caso de ser correcta la petición, creamos un punto de carga nuevo con los datos
        // introducidos en el formulario, mostrando el mensaje correspondiente.
        if(resp.status < 400){
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje(resp.body);
        }
        else{
          this.datos.cambiarMostrarMensaje(true);
          this.datos.cambiarMensaje("Error al añadir un nuevo usuario.");
        }
        this.router.navigate(['chargerpoints']);
      },
      err => {
        console.log("Error al crear: "+ err.message);
        this.errorMensaje = err.error;
        throw err;
      }
    )
  }
}
