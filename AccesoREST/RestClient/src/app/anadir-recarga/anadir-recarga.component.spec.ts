import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnadirRecargaComponent } from './anadir-recarga.component';

describe('AnadirRecargaComponent', () => {
  let component: AnadirRecargaComponent;
  let fixture: ComponentFixture<AnadirRecargaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnadirRecargaComponent]
    });
    fixture = TestBed.createComponent(AnadirRecargaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
