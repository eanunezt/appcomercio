import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransportadorService } from '../service/transportador.service';
import { ITransportador, Transportador } from '../transportador.model';

import { TransportadorUpdateComponent } from './transportador-update.component';

describe('Transportador Management Update Component', () => {
  let comp: TransportadorUpdateComponent;
  let fixture: ComponentFixture<TransportadorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transportadorService: TransportadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TransportadorUpdateComponent],
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
      .overrideTemplate(TransportadorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransportadorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transportadorService = TestBed.inject(TransportadorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const transportador: ITransportador = { id: 456 };

      activatedRoute.data = of({ transportador });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(transportador));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transportador>>();
      const transportador = { id: 123 };
      jest.spyOn(transportadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transportador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transportador }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(transportadorService.update).toHaveBeenCalledWith(transportador);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transportador>>();
      const transportador = new Transportador();
      jest.spyOn(transportadorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transportador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transportador }));
      saveSubject.complete();

      // THEN
      expect(transportadorService.create).toHaveBeenCalledWith(transportador);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transportador>>();
      const transportador = { id: 123 };
      jest.spyOn(transportadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transportador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transportadorService.update).toHaveBeenCalledWith(transportador);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
