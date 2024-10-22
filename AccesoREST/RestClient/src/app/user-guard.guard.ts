import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import {Injectable} from "@angular/core";
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
/**
export const userGuardGuard: CanActivateFn = (route, state) => {
  return true;
};
 */
export class userGuardGuard implements CanActivate{

  constructor(private cookieService: CookieService, private router: Router){

  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const cookie = this.cookieService.check('token')

    // Comprueba si en la cookie está el token JWT, es decir, si se ha llevado a cabo la autenticación
    // de forma correcta o no. En caso de que no sea así, te redirige a la pagina del login. Si sí que se ha
    // llevado a cabo la autenticación correctamente, devuelve true y el canActivate te permitirá acceder
    // a la página a la que se estaba intentando entrar.
    if(!cookie){
      return this.router.parseUrl('/login')
    } else{
      return true
    }

  }
}


