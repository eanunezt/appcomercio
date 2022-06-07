import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IItemOrden, ItemOrden } from '../item-orden.model';
import { ItemOrdenService } from '../service/item-orden.service';
import { IOrden } from 'app/entities/orden/orden.model';
import { OrdenService } from 'app/entities/orden/service/orden.service';

@Component({
  selector: 'jhi-item-orden-update',
  templateUrl: './item-orden-update.component.html',
})
export class ItemOrdenUpdateComponent implements OnInit {
  isSaving = false;

  ordensSharedCollection: IOrden[] = [];

  editForm = this.fb.group({
    id: [],
    item: [null, [Validators.required]],
    cantidad: [null, [Validators.required]],
    valorUnidad: [null, [Validators.required]],
    valor: [null, [Validators.required]],
    codProducto: [null, [Validators.required]],
    nomProdcuto: [null, [Validators.required]],
    orden: [],
  });

  constructor(
    protected itemOrdenService: ItemOrdenService,
    protected ordenService: OrdenService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemOrden }) => {
      this.updateForm(itemOrden);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemOrden = this.createFromForm();
    if (itemOrden.id !== undefined) {
      this.subscribeToSaveResponse(this.itemOrdenService.update(itemOrden));
    } else {
      this.subscribeToSaveResponse(this.itemOrdenService.create(itemOrden));
    }
  }

  trackOrdenById(index: number, item: IOrden): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemOrden>>): void {
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

  protected updateForm(itemOrden: IItemOrden): void {
    this.editForm.patchValue({
      id: itemOrden.id,
      item: itemOrden.item,
      cantidad: itemOrden.cantidad,
      valorUnidad: itemOrden.valorUnidad,
      valor: itemOrden.valor,
      codProducto: itemOrden.codProducto,
      nomProdcuto: itemOrden.nomProdcuto,
      orden: itemOrden.orden,
    });

    this.ordensSharedCollection = this.ordenService.addOrdenToCollectionIfMissing(this.ordensSharedCollection, itemOrden.orden);
  }

  protected loadRelationshipsOptions(): void {
    this.ordenService
      .query()
      .pipe(map((res: HttpResponse<IOrden[]>) => res.body ?? []))
      .pipe(map((ordens: IOrden[]) => this.ordenService.addOrdenToCollectionIfMissing(ordens, this.editForm.get('orden')!.value)))
      .subscribe((ordens: IOrden[]) => (this.ordensSharedCollection = ordens));
  }

  protected createFromForm(): IItemOrden {
    return {
      ...new ItemOrden(),
      id: this.editForm.get(['id'])!.value,
      item: this.editForm.get(['item'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      valorUnidad: this.editForm.get(['valorUnidad'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      codProducto: this.editForm.get(['codProducto'])!.value,
      nomProdcuto: this.editForm.get(['nomProdcuto'])!.value,
      orden: this.editForm.get(['orden'])!.value,
    };
  }
}
