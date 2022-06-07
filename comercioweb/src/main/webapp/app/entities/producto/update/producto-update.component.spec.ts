import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductoService } from '../service/producto.service';
import { IProducto, Producto } from '../producto.model';
import { ITipoProducto } from 'app/entities/tipo-producto/tipo-producto.model';
import { TipoProductoService } from 'app/entities/tipo-producto/service/tipo-producto.service';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';

import { ProductoUpdateComponent } from './producto-update.component';

describe('Producto Management Update Component', () => {
  let comp: ProductoUpdateComponent;
  let fixture: ComponentFixture<ProductoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productoService: ProductoService;
  let tipoProductoService: TipoProductoService;
  let proveedorService: ProveedorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductoUpdateComponent],
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
      .overrideTemplate(ProductoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productoService = TestBed.inject(ProductoService);
    tipoProductoService = TestBed.inject(TipoProductoService);
    proveedorService = TestBed.inject(ProveedorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call tipoProducto query and add missing value', () => {
      const producto: IProducto = { id: 456 };
      const tipoProducto: ITipoProducto = { id: 65390 };
      producto.tipoProducto = tipoProducto;

      const tipoProductoCollection: ITipoProducto[] = [{ id: 77142 }];
      jest.spyOn(tipoProductoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoProductoCollection })));
      const expectedCollection: ITipoProducto[] = [tipoProducto, ...tipoProductoCollection];
      jest.spyOn(tipoProductoService, 'addTipoProductoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(tipoProductoService.query).toHaveBeenCalled();
      expect(tipoProductoService.addTipoProductoToCollectionIfMissing).toHaveBeenCalledWith(tipoProductoCollection, tipoProducto);
      expect(comp.tipoProductosCollection).toEqual(expectedCollection);
    });

    it('Should call proveedor query and add missing value', () => {
      const producto: IProducto = { id: 456 };
      const proveedor: IProveedor = { id: 1713 };
      producto.proveedor = proveedor;

      const proveedorCollection: IProveedor[] = [{ id: 89565 }];
      jest.spyOn(proveedorService, 'query').mockReturnValue(of(new HttpResponse({ body: proveedorCollection })));
      const expectedCollection: IProveedor[] = [proveedor, ...proveedorCollection];
      jest.spyOn(proveedorService, 'addProveedorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(proveedorService.query).toHaveBeenCalled();
      expect(proveedorService.addProveedorToCollectionIfMissing).toHaveBeenCalledWith(proveedorCollection, proveedor);
      expect(comp.proveedorsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const producto: IProducto = { id: 456 };
      const tipoProducto: ITipoProducto = { id: 23791 };
      producto.tipoProducto = tipoProducto;
      const proveedor: IProveedor = { id: 21877 };
      producto.proveedor = proveedor;

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(producto));
      expect(comp.tipoProductosCollection).toContain(tipoProducto);
      expect(comp.proveedorsCollection).toContain(proveedor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = { id: 123 };
      jest.spyOn(productoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: producto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productoService.update).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = new Producto();
      jest.spyOn(productoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: producto }));
      saveSubject.complete();

      // THEN
      expect(productoService.create).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = { id: 123 };
      jest.spyOn(productoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productoService.update).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTipoProductoById', () => {
      it('Should return tracked TipoProducto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoProductoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProveedorById', () => {
      it('Should return tracked Proveedor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProveedorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
