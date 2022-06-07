import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProducto, Producto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { ITipoProducto } from 'app/entities/tipo-producto/tipo-producto.model';
import { TipoProductoService } from 'app/entities/tipo-producto/service/tipo-producto.service';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { EstadoProducto } from 'app/entities/enumerations/estado-producto.model';

@Component({
  selector: 'jhi-producto-update',
  templateUrl: './producto-update.component.html',
})
export class ProductoUpdateComponent implements OnInit {
  isSaving = false;
  estadoProductoValues = Object.keys(EstadoProducto);

  tipoProductosCollection: ITipoProducto[] = [];
  proveedorsCollection: IProveedor[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    codigo: [null, [Validators.required]],
    valor: [null, [Validators.required]],
    estado: [null, [Validators.required]],
    tipoProducto: [],
    proveedor: [],
  });

  constructor(
    protected productoService: ProductoService,
    protected tipoProductoService: TipoProductoService,
    protected proveedorService: ProveedorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.updateForm(producto);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const producto = this.createFromForm();
    if (producto.id !== undefined) {
      this.subscribeToSaveResponse(this.productoService.update(producto));
    } else {
      this.subscribeToSaveResponse(this.productoService.create(producto));
    }
  }

  trackTipoProductoById(index: number, item: ITipoProducto): number {
    return item.id!;
  }

  trackProveedorById(index: number, item: IProveedor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducto>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(producto: IProducto): void {
    this.editForm.patchValue({
      id: producto.id,
      nombre: producto.nombre,
      codigo: producto.codigo,
      valor: producto.valor,
      estado: producto.estado,
      tipoProducto: producto.tipoProducto,
      proveedor: producto.proveedor,
    });

    this.tipoProductosCollection = this.tipoProductoService.addTipoProductoToCollectionIfMissing(
      this.tipoProductosCollection,
      producto.tipoProducto
    );
    this.proveedorsCollection = this.proveedorService.addProveedorToCollectionIfMissing(this.proveedorsCollection, producto.proveedor);
  }

  protected loadRelationshipsOptions(): void {
    this.tipoProductoService
      .query({ filter: 'producto-is-null' })
      .pipe(map((res: HttpResponse<ITipoProducto[]>) => res.body ?? []))
      .pipe(
        map((tipoProductos: ITipoProducto[]) =>
          this.tipoProductoService.addTipoProductoToCollectionIfMissing(tipoProductos, this.editForm.get('tipoProducto')!.value)
        )
      )
      .subscribe((tipoProductos: ITipoProducto[]) => (this.tipoProductosCollection = tipoProductos));

    this.proveedorService
      .query({ filter: 'producto-is-null' })
      .pipe(map((res: HttpResponse<IProveedor[]>) => res.body ?? []))
      .pipe(
        map((proveedors: IProveedor[]) =>
          this.proveedorService.addProveedorToCollectionIfMissing(proveedors, this.editForm.get('proveedor')!.value)
        )
      )
      .subscribe((proveedors: IProveedor[]) => (this.proveedorsCollection = proveedors));
  }

  protected createFromForm(): IProducto {
    return {
      ...new Producto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      tipoProducto: this.editForm.get(['tipoProducto'])!.value,
      proveedor: this.editForm.get(['proveedor'])!.value,
    };
  }
}
