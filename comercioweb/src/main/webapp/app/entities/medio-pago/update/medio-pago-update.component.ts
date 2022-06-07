import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMedioPago, MedioPago } from '../medio-pago.model';
import { MedioPagoService } from '../service/medio-pago.service';

@Component({
  selector: 'jhi-medio-pago-update',
  templateUrl: './medio-pago-update.component.html',
})
export class MedioPagoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    codigo: [null, [Validators.required]],
  });

  constructor(protected medioPagoService: MedioPagoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medioPago }) => {
      this.updateForm(medioPago);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medioPago = this.createFromForm();
    if (medioPago.id !== undefined) {
      this.subscribeToSaveResponse(this.medioPagoService.update(medioPago));
    } else {
      this.subscribeToSaveResponse(this.medioPagoService.create(medioPago));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedioPago>>): void {
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

  protected updateForm(medioPago: IMedioPago): void {
    this.editForm.patchValue({
      id: medioPago.id,
      nombre: medioPago.nombre,
      codigo: medioPago.codigo,
    });
  }

  protected createFromForm(): IMedioPago {
    return {
      ...new MedioPago(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
    };
  }
}
