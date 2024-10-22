import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import {PlugType, PuntoDeCarga, Recarga, Usuario, Vehiculo} from './app.model';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})


export class ClienteApiRestService {

  private static readonly BASE_URI_chargerpoints = 'http://localhost:8000/api/chargerpoints';

  // El puerto 8081 es el de la API de autenticación.
  private static readonly BASE_URI_login = 'http://localhost:8000/api/login';

  private static readonly BASE_URI_users = 'http://localhost:8000/api/users';

  private static readonly BASE_URI_vehicles = 'http://localhost:8000/api/vehicles';

  private static readonly BASE_URI_recharge = 'http://localhost:8000/api/recharge';

  constructor(private http: HttpClient, private cookieService: CookieService) { }


  getAllUsuarios(): Observable<HttpResponse<Usuario[]>>{
    let url = ClienteApiRestService.BASE_URI_users + "/users";
    let token = this.cookieService.get('token');

    let headers = {Authorization: `Bearer ${token}`};
    return this.http.get<Usuario[]>(url, {headers, observe: 'response'});
  }

  getUsuariosEnabled(): Observable<HttpResponse<Usuario[]>>{
    let url = ClienteApiRestService.BASE_URI_users + "/users?enabled=true";
    return this.http.get<Usuario[]>(url, {observe: 'response'});
  }

  getUsuariosDisabled(): Observable<HttpResponse<Usuario[]>>{
    let url = ClienteApiRestService.BASE_URI_users + "/users?enabled=false";
    return this.http.get<Usuario[]>(url, {observe: 'response'});
  }

  getUsuario(id: Number): Observable<HttpResponse<Usuario>>{
    let url = ClienteApiRestService.BASE_URI_users + "/users/" + id;
    return this.http.get<Usuario>(url, {observe: 'response'});
  }

  getUsuarioByEmail(email: String) : Observable<HttpResponse<Usuario>>{
   let url = ClienteApiRestService.BASE_URI_users + "/users?email=" + email;
   return this.http.get<Usuario>(url, {observe: 'response'});
  }

  anadirUsuario(usuario: Usuario): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_users + "/users";
    return this.http.post(url, usuario, {observe: 'response', responseType: 'text'});
  }

  modificarUsuario(id: String, usuario: Usuario): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_users + "/users/" + id ;
    return this.http.put(url, usuario, {observe: 'response', responseType: 'text'});
  }

  borrarUsuario(id: String): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_users + "/users/" + id;
    let token = this.cookieService.get('token');

    let headers = {Authorization: `Bearer ${token}`};
    return this.http.delete(url, {headers, observe: 'response', responseType : 'text'});
  }

  getAllVehiculos(): Observable<HttpResponse<Vehiculo[]>>{
    let url = ClienteApiRestService.BASE_URI_vehicles + "/vehicles";
    return this.http.get<Vehiculo[]>(url, {observe: 'response'});
  }

  borrarVehiculo(id: String): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_vehicles + "/vehicles/" + id;
    let token = this.cookieService.get('token');

    let headers = {Authorization: `Bearer ${token}`};
    return this.http.delete(url, {headers, observe: 'response', responseType : 'text'});
  }

  anadirVehiculo(vehiculo: Vehiculo): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_vehicles + "/vehicles";
    let token = this.cookieService.get('token');

    // Rescatamos el token jwt y lo añadimos en las cabeceras de la request.
    let headers = {Authorization: `Bearer ${token}`};
    return this.http.post(url, vehiculo, {headers, observe: 'response', responseType: 'text'});
  }

  getVehiculoByIdUsuario(id : String): Observable<HttpResponse<Vehiculo[]>>{
    let url = ClienteApiRestService.BASE_URI_vehicles + "/vehicles/" + id;
    return this.http.get<Vehiculo[]>(url, {observe: 'response'});
  }

  getPuntosCarga(): Observable<HttpResponse<PuntoDeCarga[]>>{
    let url = ClienteApiRestService.BASE_URI_chargerpoints + "/chargerpoints";
    return this.http.get<PuntoDeCarga[]>(url, {observe: 'response'});
  }

  getPuntoCarga(id: String): Observable<HttpResponse<PuntoDeCarga>>{
    let url = ClienteApiRestService.BASE_URI_chargerpoints + "/chargerpoints/" + id;
    return this.http.get<PuntoDeCarga>(url, {observe: 'response'});
  }

  anadirPuntoCarga(puntoCarga : PuntoDeCarga): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_chargerpoints + "/chargerpoints";
    return this.http.post(url, puntoCarga, {observe: 'response', responseType: 'text'});
  }

  modificarPuntoCarga(id: String, puntoCarga: PuntoDeCarga): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_chargerpoints + "/chargerpoints/" + id ;
    return this.http.put(url, puntoCarga, {observe: 'response', responseType: 'text'});
  }

  getVehiculosPorUsuario(id: String): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_vehicles +"/users/" + id + "/vehicles";
    return this.http.get<Vehiculo[]>(url, {observe: 'response'});
  }

  getPuntosRecargaPorPlugType(id: String): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_vehicles +"/vehicles/" + id + "/chargerpoints";
    let token = this.cookieService.get('token');

    let headers = {Authorization: `Bearer ${token}`};
    return this.http.get<Vehiculo[]>(url, {observe: 'response'});
  }

  login(usuario: Usuario): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_login + "/login";
    return this.http.post(url, usuario, {observe: 'response', responseType: 'text'});
  }

  getRecargasDeUsuario(id: Number): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_recharge + "/recharge?userId=" + id;
    return this.http.get<Recarga[]>(url, {observe: 'response'});
  }

  getRecargaPorId(id: Number): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_recharge + "/recharge/" + id;
    return this.http.get<Recarga>(url, {observe: 'response'})
  }

  anadirRecarga(recharge: Recarga): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_recharge + "/recharge";
    let token = this.cookieService.get('token');

    let headers = {Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'};
    return this.http.post(url, recharge, {headers, observe: 'response', responseType: 'text'})
  }

  modificarRecarga(id: Number, accion: String): Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI_recharge + "/recharge/"+ id + "?accion=" + accion;
    let token = this.cookieService.get('token');

    let body = {}

    let headers = {Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'};
    return this.http.put(url, body, {headers, observe: 'response', responseType: 'text'});
  }
}
