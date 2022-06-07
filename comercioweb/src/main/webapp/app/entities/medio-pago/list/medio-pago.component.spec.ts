import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MedioPagoService } from '../service/medio-pago.service';

import { MedioPagoComponent } from './medio-pago.component';

describe('MedioPago Management Component', () => {
  let comp: MedioPagoComponent;
  let fixture: ComponentFixture<MedioPagoComponent>;
  let service: MedioPagoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MedioPagoComponent],
    })
      .overrideTemplate(MedioPagoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedioPagoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MedioPagoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.medioPagos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
