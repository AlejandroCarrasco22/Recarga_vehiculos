import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnadirVehiculoComponent } from './anadir-vehiculo.component';

describe('AnadirVehiculoComponent', () => {
  let component: AnadirVehiculoComponent;
  let fixture: ComponentFixture<AnadirVehiculoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnadirVehiculoComponent]
    });
    fixture = TestBed.createComponent(AnadirVehiculoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
