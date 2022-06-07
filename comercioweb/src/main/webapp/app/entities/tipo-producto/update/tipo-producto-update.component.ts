import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoProducto, TipoProducto } from '../tipo-producto.model';
import { TipoProductoService } from '../service/tipo-producto.service';

@Component({
  selector: 'jhi-tipo-producto-update',
  templateUrl: './tipo-producto-update.component.html',
})
export class TipoProductoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    codigo: [null, [Validators.required]],
  });

  constructor(protected tipoProductoService: TipoProductoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoProducto }) => {
      this.updateForm(tipoProducto);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoProducto = this.createFromForm();
    if (tipoProducto.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoProductoService.update(tipoProducto));
    } else {
      this.subscribeToSaveResponse(this.tipoProductoService.create(tipoProducto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoProducto>>): void {
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

  protected updateForm(tipoProducto: ITipoProducto): void {
    this.editForm.patchValue({
      id: tipoProducto.id,
      nombre: tipoProducto.nombre,
      codigo: tipoProducto.codigo,
    });
  }

  protected createFromForm(): ITipoProducto {
    return {
      ...new TipoProducto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
    };
  }
}
