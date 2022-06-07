import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MedioPagoDetailComponent } from './medio-pago-detail.component';

describe('MedioPago Management Detail Component', () => {
  let comp: MedioPagoDetailComponent;
  let fixture: ComponentFixture<MedioPagoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MedioPagoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ medioPago: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MedioPagoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MedioPagoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load medioPago on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.medioPago).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
