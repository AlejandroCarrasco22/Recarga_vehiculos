import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Injectable()
export class JwtInterceptorInterceptor implements HttpInterceptor {

  constructor(private cookieService: CookieService, private router: Router) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token: string = this.cookieService.get('token'); // Consulta si se tiene un token
    let req = request;

    if(token){
      // Clona el encabezado de la request y le añade el token.
      req = request.clone({
        setHeaders : {

          authorization: `${token}`
        }
      });
    }
    return next.handle(req);
  }
}
