import { Component } from '@angular/core';
import { Usuario } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../shared/data.service';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usuarioVacio ={
    id:0,
    name:"",
    firstName:"",
    lastName:"",
    email:"",
    password:"",
    paymentCard:"",
    enabled: false,
    createdAt: new Date("0000-00-00"),
    updatedAt: new Date("0000-00-00"),
    vehiculoCollection: []
  };

  usuario = this.usuarioVacio as Usuario;

  constructor(private ruta:ActivatedRoute, private router:Router, private clienteApiRest:ClienteApiRestService, private datos:DataService,
                  private cookiesService: CookieService) {}

  onSubmit(): any{
    console.log("En inicio de sesion");
      this.clienteApiRest.login(this.usuario)
      .subscribe( resp => {
                  console.log("Log in correcto");
                  this.cookiesService.set('token', resp.body);
                  this.router.navigate(['index']);
        },
        err => {
          console.log("Error al logear al usuario: " + err.message);
          throw err;
        }
      )
  }






}
