import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrdenService } from '../service/orden.service';
import { IOrden, Orden } from '../orden.model';
import { IMedioPago } from 'app/entities/medio-pago/medio-pago.model';
import { MedioPagoService } from 'app/entities/medio-pago/service/medio-pago.service';
import { ITransportador } from 'app/entities/transportador/transportador.model';
import { TransportadorService } from 'app/entities/transportador/service/transportador.service';

import { OrdenUpdateComponent } from './orden-update.component';

describe('Orden Management Update Component', () => {
  let comp: OrdenUpdateComponent;
  let fixture: ComponentFixture<OrdenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ordenService: OrdenService;
  let medioPagoService: MedioPagoService;
  let transportadorService: TransportadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrdenUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrdenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordenService = TestBed.inject(OrdenService);
    medioPagoService = TestBed.inject(MedioPagoService);
    transportadorService = TestBed.inject(TransportadorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call mediPago query and add missing value', () => {
      const orden: IOrden = { id: 456 };
      const mediPago: IMedioPago = { id: 63661 };
      orden.mediPago = mediPago;

      const mediPagoCollection: IMedioPago[] = [{ id: 94655 }];
      jest.spyOn(medioPagoService, 'query').mockReturnValue(of(new HttpResponse({ body: mediPagoCollection })));
      const expectedCollection: IMedioPago[] = [mediPago, ...mediPagoCollection];
      jest.spyOn(medioPagoService, 'addMedioPagoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(medioPagoService.query).toHaveBeenCalled();
      expect(medioPagoService.addMedioPagoToCollectionIfMissing).toHaveBeenCalledWith(mediPagoCollection, mediPago);
      expect(comp.mediPagosCollection).toEqual(expectedCollection);
    });

    it('Should call transportador query and add missing value', () => {
      const orden: IOrden = { id: 456 };
      const transportador: ITransportador = { id: 44971 };
      orden.transportador = transportador;

      const transportadorCollection: ITransportador[] = [{ id: 25129 }];
      jest.spyOn(transportadorService, 'query').mockReturnValue(of(new HttpResponse({ body: transportadorCollection })));
      const expectedCollection: ITransportador[] = [transportador, ...transportadorCollection];
      jest.spyOn(transportadorService, 'addTransportadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(transportadorService.query).toHaveBeenCalled();
      expect(transportadorService.addTransportadorToCollectionIfMissing).toHaveBeenCalledWith(transportadorCollection, transportador);
      expect(comp.transportadorsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orden: IOrden = { id: 456 };
      const mediPago: IMedioPago = { id: 51103 };
      orden.mediPago = mediPago;
      const transportador: ITransportador = { id: 19596 };
      orden.transportador = transportador;

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orden));
      expect(comp.mediPagosCollection).toContain(mediPago);
      expect(comp.transportadorsCollection).toContain(transportador);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Orden>>();
      const orden = { id: 123 };
      jest.spyOn(ordenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orden }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordenService.update).toHaveBeenCalledWith(orden);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Orden>>();
      const orden = new Orden();
      jest.spyOn(ordenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orden }));
      saveSubject.complete();

      // THEN
      expect(ordenService.create).toHaveBeenCalledWith(orden);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Orden>>();
      const orden = { id: 123 };
      jest.spyOn(ordenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordenService.update).toHaveBeenCalledWith(orden);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMedioPagoById', () => {
      it('Should return tracked MedioPago primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMedioPagoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTransportadorById', () => {
      it('Should return tracked Transportador primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransportadorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
