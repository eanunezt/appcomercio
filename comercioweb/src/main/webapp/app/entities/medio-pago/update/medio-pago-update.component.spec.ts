import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MedioPagoService } from '../service/medio-pago.service';
import { IMedioPago, MedioPago } from '../medio-pago.model';

import { MedioPagoUpdateComponent } from './medio-pago-update.component';

describe('MedioPago Management Update Component', () => {
  let comp: MedioPagoUpdateComponent;
  let fixture: ComponentFixture<MedioPagoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medioPagoService: MedioPagoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MedioPagoUpdateComponent],
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
      .overrideTemplate(MedioPagoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedioPagoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medioPagoService = TestBed.inject(MedioPagoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const medioPago: IMedioPago = { id: 456 };

      activatedRoute.data = of({ medioPago });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(medioPago));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MedioPago>>();
      const medioPago = { id: 123 };
      jest.spyOn(medioPagoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medioPago });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medioPago }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(medioPagoService.update).toHaveBeenCalledWith(medioPago);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MedioPago>>();
      const medioPago = new MedioPago();
      jest.spyOn(medioPagoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medioPago });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medioPago }));
      saveSubject.complete();

      // THEN
      expect(medioPagoService.create).toHaveBeenCalledWith(medioPago);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MedioPago>>();
      const medioPago = { id: 123 };
      jest.spyOn(medioPagoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medioPago });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medioPagoService.update).toHaveBeenCalledWith(medioPago);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
