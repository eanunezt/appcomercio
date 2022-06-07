import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DireccionService } from '../service/direccion.service';
import { IDireccion, Direccion } from '../direccion.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

import { DireccionUpdateComponent } from './direccion-update.component';

describe('Direccion Management Update Component', () => {
  let comp: DireccionUpdateComponent;
  let fixture: ComponentFixture<DireccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let direccionService: DireccionService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DireccionUpdateComponent],
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
      .overrideTemplate(DireccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DireccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    direccionService = TestBed.inject(DireccionService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const direccion: IDireccion = { id: 456 };
      const cliente: ICliente = { id: 43461 };
      direccion.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 83632 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, ...additionalClientes);
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const direccion: IDireccion = { id: 456 };
      const cliente: ICliente = { id: 79839 };
      direccion.cliente = cliente;

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(direccion));
      expect(comp.clientesSharedCollection).toContain(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Direccion>>();
      const direccion = { id: 123 };
      jest.spyOn(direccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direccion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(direccionService.update).toHaveBeenCalledWith(direccion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Direccion>>();
      const direccion = new Direccion();
      jest.spyOn(direccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direccion }));
      saveSubject.complete();

      // THEN
      expect(direccionService.create).toHaveBeenCalledWith(direccion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Direccion>>();
      const direccion = { id: 123 };
      jest.spyOn(direccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(direccionService.update).toHaveBeenCalledWith(direccion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClienteById', () => {
      it('Should return tracked Cliente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
