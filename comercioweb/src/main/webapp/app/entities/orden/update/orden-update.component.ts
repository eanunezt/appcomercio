import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrden, Orden } from '../orden.model';
import { OrdenService } from '../service/orden.service';
import { IMedioPago } from 'app/entities/medio-pago/medio-pago.model';
import { MedioPagoService } from 'app/entities/medio-pago/service/medio-pago.service';
import { ITransportador } from 'app/entities/transportador/transportador.model';
import { TransportadorService } from 'app/entities/transportador/service/transportador.service';
import { EstadoOrden } from 'app/entities/enumerations/estado-orden.model';

@Component({
  selector: 'jhi-orden-update',
  templateUrl: './orden-update.component.html',
})
export class OrdenUpdateComponent implements OnInit {
  isSaving = false;
  estadoOrdenValues = Object.keys(EstadoOrden);

  mediPagosCollection: IMedioPago[] = [];
  transportadorsCollection: ITransportador[] = [];

  editForm = this.fb.group({
    id: [],
    fechaRegistro: [null, [Validators.required]],
    fechaEntregaEstimada: [null, [Validators.required]],
    fechaEnntregaReal: [null, [Validators.required]],
    descripcion: [null, [Validators.required]],
    codigo: [null, [Validators.required]],
    valorTotal: [null, [Validators.required]],
    factura: [null, [Validators.required]],
    estado: [null, [Validators.required]],
    codCliente: [null, [Validators.required]],
    datosCliente: [null, [Validators.required]],
    mediPago: [],
    transportador: [],
  });

  constructor(
    protected ordenService: OrdenService,
    protected medioPagoService: MedioPagoService,
    protected transportadorService: TransportadorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orden }) => {
      if (orden.id === undefined) {
        const today = dayjs().startOf('day');
        orden.fechaRegistro = today;
        orden.fechaEntregaEstimada = today;
        orden.fechaEnntregaReal = today;
      }

      this.updateForm(orden);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orden = this.createFromForm();
    if (orden.id !== undefined) {
      this.subscribeToSaveResponse(this.ordenService.update(orden));
    } else {
      this.subscribeToSaveResponse(this.ordenService.create(orden));
    }
  }

  trackMedioPagoById(index: number, item: IMedioPago): number {
    return item.id!;
  }

  trackTransportadorById(index: number, item: ITransportador): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrden>>): void {
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

  protected updateForm(orden: IOrden): void {
    this.editForm.patchValue({
      id: orden.id,
      fechaRegistro: orden.fechaRegistro ? orden.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      fechaEntregaEstimada: orden.fechaEntregaEstimada ? orden.fechaEntregaEstimada.format(DATE_TIME_FORMAT) : null,
      fechaEnntregaReal: orden.fechaEnntregaReal ? orden.fechaEnntregaReal.format(DATE_TIME_FORMAT) : null,
      descripcion: orden.descripcion,
      codigo: orden.codigo,
      valorTotal: orden.valorTotal,
      factura: orden.factura,
      estado: orden.estado,
      codCliente: orden.codCliente,
      datosCliente: orden.datosCliente,
      mediPago: orden.mediPago,
      transportador: orden.transportador,
    });

    this.mediPagosCollection = this.medioPagoService.addMedioPagoToCollectionIfMissing(this.mediPagosCollection, orden.mediPago);
    this.transportadorsCollection = this.transportadorService.addTransportadorToCollectionIfMissing(
      this.transportadorsCollection,
      orden.transportador
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medioPagoService
      .query({ filter: 'orden-is-null' })
      .pipe(map((res: HttpResponse<IMedioPago[]>) => res.body ?? []))
      .pipe(
        map((medioPagos: IMedioPago[]) =>
          this.medioPagoService.addMedioPagoToCollectionIfMissing(medioPagos, this.editForm.get('mediPago')!.value)
        )
      )
      .subscribe((medioPagos: IMedioPago[]) => (this.mediPagosCollection = medioPagos));

    this.transportadorService
      .query({ filter: 'orden-is-null' })
      .pipe(map((res: HttpResponse<ITransportador[]>) => res.body ?? []))
      .pipe(
        map((transportadors: ITransportador[]) =>
          this.transportadorService.addTransportadorToCollectionIfMissing(transportadors, this.editForm.get('transportador')!.value)
        )
      )
      .subscribe((transportadors: ITransportador[]) => (this.transportadorsCollection = transportadors));
  }

  protected createFromForm(): IOrden {
    return {
      ...new Orden(),
      id: this.editForm.get(['id'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaEntregaEstimada: this.editForm.get(['fechaEntregaEstimada'])!.value
        ? dayjs(this.editForm.get(['fechaEntregaEstimada'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaEnntregaReal: this.editForm.get(['fechaEnntregaReal'])!.value
        ? dayjs(this.editForm.get(['fechaEnntregaReal'])!.value, DATE_TIME_FORMAT)
        : undefined,
      descripcion: this.editForm.get(['descripcion'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      valorTotal: this.editForm.get(['valorTotal'])!.value,
      factura: this.editForm.get(['factura'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      codCliente: this.editForm.get(['codCliente'])!.value,
      datosCliente: this.editForm.get(['datosCliente'])!.value,
      mediPago: this.editForm.get(['mediPago'])!.value,
      transportador: this.editForm.get(['transportador'])!.value,
    };
  }
}
