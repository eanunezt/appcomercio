import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITransportador, Transportador } from '../transportador.model';
import { TransportadorService } from '../service/transportador.service';
import { EstadoTransportador } from 'app/entities/enumerations/estado-transportador.model';

@Component({
  selector: 'jhi-transportador-update',
  templateUrl: './transportador-update.component.html',
})
export class TransportadorUpdateComponent implements OnInit {
  isSaving = false;
  estadoTransportadorValues = Object.keys(EstadoTransportador);

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    codigo: [null, [Validators.required]],
    estado: [null, [Validators.required]],
  });

  constructor(protected transportadorService: TransportadorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transportador }) => {
      this.updateForm(transportador);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transportador = this.createFromForm();
    if (transportador.id !== undefined) {
      this.subscribeToSaveResponse(this.transportadorService.update(transportador));
    } else {
      this.subscribeToSaveResponse(this.transportadorService.create(transportador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransportador>>): void {
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

  protected updateForm(transportador: ITransportador): void {
    this.editForm.patchValue({
      id: transportador.id,
      nombre: transportador.nombre,
      codigo: transportador.codigo,
      estado: transportador.estado,
    });
  }

  protected createFromForm(): ITransportador {
    return {
      ...new Transportador(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
