import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemOrdenService } from '../service/item-orden.service';
import { IItemOrden, ItemOrden } from '../item-orden.model';
import { IOrden } from 'app/entities/orden/orden.model';
import { OrdenService } from 'app/entities/orden/service/orden.service';

import { ItemOrdenUpdateComponent } from './item-orden-update.component';

describe('ItemOrden Management Update Component', () => {
  let comp: ItemOrdenUpdateComponent;
  let fixture: ComponentFixture<ItemOrdenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemOrdenService: ItemOrdenService;
  let ordenService: OrdenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemOrdenUpdateComponent],
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
      .overrideTemplate(ItemOrdenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemOrdenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemOrdenService = TestBed.inject(ItemOrdenService);
    ordenService = TestBed.inject(OrdenService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Orden query and add missing value', () => {
      const itemOrden: IItemOrden = { id: 456 };
      const orden: IOrden = { id: 20082 };
      itemOrden.orden = orden;

      const ordenCollection: IOrden[] = [{ id: 80284 }];
      jest.spyOn(ordenService, 'query').mockReturnValue(of(new HttpResponse({ body: ordenCollection })));
      const additionalOrdens = [orden];
      const expectedCollection: IOrden[] = [...additionalOrdens, ...ordenCollection];
      jest.spyOn(ordenService, 'addOrdenToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemOrden });
      comp.ngOnInit();

      expect(ordenService.query).toHaveBeenCalled();
      expect(ordenService.addOrdenToCollectionIfMissing).toHaveBeenCalledWith(ordenCollection, ...additionalOrdens);
      expect(comp.ordensSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const itemOrden: IItemOrden = { id: 456 };
      const orden: IOrden = { id: 91229 };
      itemOrden.orden = orden;

      activatedRoute.data = of({ itemOrden });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(itemOrden));
      expect(comp.ordensSharedCollection).toContain(orden);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemOrden>>();
      const itemOrden = { id: 123 };
      jest.spyOn(itemOrdenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemOrden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemOrden }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemOrdenService.update).toHaveBeenCalledWith(itemOrden);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemOrden>>();
      const itemOrden = new ItemOrden();
      jest.spyOn(itemOrdenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemOrden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemOrden }));
      saveSubject.complete();

      // THEN
      expect(itemOrdenService.create).toHaveBeenCalledWith(itemOrden);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemOrden>>();
      const itemOrden = { id: 123 };
      jest.spyOn(itemOrdenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemOrden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemOrdenService.update).toHaveBeenCalledWith(itemOrden);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOrdenById', () => {
      it('Should return tracked Orden primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrdenById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
