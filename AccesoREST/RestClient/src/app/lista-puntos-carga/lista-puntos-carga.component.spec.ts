import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaPuntosCargaComponent } from './lista-puntos-carga.component';

describe('ListaPuntosCargaComponent', () => {
  let component: ListaPuntosCargaComponent;
  let fixture: ComponentFixture<ListaPuntosCargaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListaPuntosCargaComponent]
    });
    fixture = TestBed.createComponent(ListaPuntosCargaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
