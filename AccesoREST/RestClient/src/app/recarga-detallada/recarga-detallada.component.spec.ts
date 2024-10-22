import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecargaDetalladaComponent } from './recarga-detallada.component';

describe('RecargaDetalladaComponent', () => {
  let component: RecargaDetalladaComponent;
  let fixture: ComponentFixture<RecargaDetalladaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecargaDetalladaComponent]
    });
    fixture = TestBed.createComponent(RecargaDetalladaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
