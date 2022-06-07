import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProveedor, Proveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';

@Component({
  selector: 'jhi-proveedor-update',
  templateUrl: './proveedor-update.component.html',
})
export class ProveedorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    codigo: [null, [Validators.required]],
  });

  constructor(protected proveedorService: ProveedorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedor }) => {
      this.updateForm(proveedor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proveedor = this.createFromForm();
    if (proveedor.id !== undefined) {
      this.subscribeToSaveResponse(this.proveedorService.update(proveedor));
    } else {
      this.subscribeToSaveResponse(this.proveedorService.create(proveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProveedor>>): void {
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

  protected updateForm(proveedor: IProveedor): void {
    this.editForm.patchValue({
      id: proveedor.id,
      nombre: proveedor.nombre,
      codigo: proveedor.codigo,
    });
  }

  protected createFromForm(): IProveedor {
    return {
      ...new Proveedor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
    };
  }
}
