import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ListaUsuariosComponent } from './lista-usuarios/lista-usuarios.component';
import { EditarUsuarioComponent } from './editar-usuario/editar-usuario.component';
import { ClienteApiRestService } from './shared/cliente-api-rest.service';
import { DataService } from './shared/data.service';

import { AnadirUsuarioComponent } from './anadir-usuario/anadir-usuario.component';
import { ListaVehiculosComponent } from './lista-vehiculos/lista-vehiculos.component';
import { AnadirVehiculoComponent } from './anadir-vehiculo/anadir-vehiculo.component';
import { ListaPuntosCargaComponent } from './lista-puntos-carga/lista-puntos-carga.component';
import { EditarPuntosCargaComponent } from './editar-puntos-carga/editar-puntos-carga.component';
import { AnadirPuntoCargaComponent } from './anadir-punto-carga/anadir-punto-carga.component';
import { IndexComponent } from './index/index.component';
import { LoginComponent } from './login/login.component';
import {JwtInterceptorInterceptor } from './jwt-interceptor.interceptor';
import { ListaRecargasUsuarioComponent } from './lista-recargas-usuario/lista-recargas-usuario.component';
import { RecargaDetalladaComponent } from './recarga-detallada/recarga-detallada.component';
import { AnadirRecargaComponent } from './anadir-recarga/anadir-recarga.component';




@NgModule({
  declarations: [
    AppComponent,
    ListaUsuariosComponent,
    EditarUsuarioComponent,
    AnadirUsuarioComponent,
    ListaVehiculosComponent,
    AnadirVehiculoComponent,
    ListaPuntosCargaComponent,
    EditarPuntosCargaComponent,
    AnadirPuntoCargaComponent,
    IndexComponent,
    LoginComponent,
    ListaRecargasUsuarioComponent,
    RecargaDetalladaComponent,
    AnadirRecargaComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    ClienteApiRestService,
    DataService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
