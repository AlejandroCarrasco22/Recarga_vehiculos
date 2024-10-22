import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AnadirPuntoCargaComponent } from './anadir-punto-carga/anadir-punto-carga.component';
import { AnadirUsuarioComponent } from './anadir-usuario/anadir-usuario.component';
import { AnadirVehiculoComponent } from './anadir-vehiculo/anadir-vehiculo.component';
import { EditarPuntosCargaComponent } from './editar-puntos-carga/editar-puntos-carga.component';
import { EditarUsuarioComponent } from './editar-usuario/editar-usuario.component';
import { IndexComponent } from './index/index.component';
import { LoginComponent} from './login/login.component';
import { ListaPuntosCargaComponent } from './lista-puntos-carga/lista-puntos-carga.component';
import { ListaUsuariosComponent } from './lista-usuarios/lista-usuarios.component';
import { ListaVehiculosComponent } from './lista-vehiculos/lista-vehiculos.component';
import {userGuardGuard} from "./user-guard.guard";
import {ListaRecargasUsuarioComponent} from "./lista-recargas-usuario/lista-recargas-usuario.component";
import { RecargaDetalladaComponent } from './recarga-detallada/recarga-detallada.component';
import { AnadirRecargaComponent} from './anadir-recarga/anadir-recarga.component'

// canActivate protege todas las páginas (salvo el login) para que no sen accedidas
// sin antes autenticarse correctamente.
const routes: Routes = [
  {path: "users", component:ListaUsuariosComponent, canActivate : [userGuardGuard]},
  {path: "users/nuevo", component:AnadirUsuarioComponent, canActivate : [userGuardGuard]},
  {path: "users/:id", component:EditarUsuarioComponent, canActivate : [userGuardGuard]},
  {path: "vehicles", component:ListaVehiculosComponent, canActivate : [userGuardGuard]},
  {path: "vehicles/nuevo", component:AnadirVehiculoComponent, canActivate : [userGuardGuard]},
  {path: "chargerpoints", component: ListaPuntosCargaComponent, canActivate : [userGuardGuard]},
  {path: "chargerpoints/nuevo", component:AnadirPuntoCargaComponent, canActivate : [userGuardGuard]},
  {path: "chargerpoints/:id", component: EditarPuntosCargaComponent, canActivate : [userGuardGuard]},
  {path: "recharge", component: ListaRecargasUsuarioComponent, canActivate : [userGuardGuard]},
  {path: "recharge/nuevo", component: AnadirRecargaComponent, canActivate : [userGuardGuard]},
  {path: "recharge/:id", component: RecargaDetalladaComponent, canActivate : [userGuardGuard]},
  {path: "index", component: IndexComponent, canActivate : [userGuardGuard]},
  {path: "login", component: LoginComponent },
  {path: "**", component: IndexComponent, canActivate : [userGuardGuard] }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
