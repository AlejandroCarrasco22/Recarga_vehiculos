import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnadirPuntoCargaComponent } from './anadir-punto-carga.component';

describe('AnadirPuntoCargaComponent', () => {
  let component: AnadirPuntoCargaComponent;
  let fixture: ComponentFixture<AnadirPuntoCargaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnadirPuntoCargaComponent]
    });
    fixture = TestBed.createComponent(AnadirPuntoCargaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
