import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarPuntosCargaComponent } from './editar-puntos-carga.component';

describe('EditarPuntosCargaComponent', () => {
  let component: EditarPuntosCargaComponent;
  let fixture: ComponentFixture<EditarPuntosCargaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditarPuntosCargaComponent]
    });
    fixture = TestBed.createComponent(EditarPuntosCargaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
