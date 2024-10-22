import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaRecargasUsuarioComponent } from './lista-recargas-usuario.component';

describe('ListaRecargasUsuarioComponent', () => {
  let component: ListaRecargasUsuarioComponent;
  let fixture: ComponentFixture<ListaRecargasUsuarioComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListaRecargasUsuarioComponent]
    });
    fixture = TestBed.createComponent(ListaRecargasUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
