import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDireccion, Direccion } from '../direccion.model';
import { DireccionService } from '../service/direccion.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

@Component({
  selector: 'jhi-direccion-update',
  templateUrl: './direccion-update.component.html',
})
export class DireccionUpdateComponent implements OnInit {
  isSaving = false;

  clientesSharedCollection: ICliente[] = [];

  editForm = this.fb.group({
    id: [],
    direccion: [null, [Validators.required]],
    ciudad: [null, [Validators.required]],
    barrio: [null, [Validators.required]],
    cliente: [],
  });

  constructor(
    protected direccionService: DireccionService,
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ direccion }) => {
      this.updateForm(direccion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const direccion = this.createFromForm();
    if (direccion.id !== undefined) {
      this.subscribeToSaveResponse(this.direccionService.update(direccion));
    } else {
      this.subscribeToSaveResponse(this.direccionService.create(direccion));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDireccion>>): void {
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

  protected updateForm(direccion: IDireccion): void {
    this.editForm.patchValue({
      id: direccion.id,
      direccion: direccion.direccion,
      ciudad: direccion.ciudad,
      barrio: direccion.barrio,
      cliente: direccion.cliente,
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, direccion.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }

  protected createFromForm(): IDireccion {
    return {
      ...new Direccion(),
      id: this.editForm.get(['id'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      ciudad: this.editForm.get(['ciudad'])!.value,
      barrio: this.editForm.get(['barrio'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
