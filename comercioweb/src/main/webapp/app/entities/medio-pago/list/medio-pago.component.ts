import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedioPago } from '../medio-pago.model';
import { MedioPagoService } from '../service/medio-pago.service';
import { MedioPagoDeleteDialogComponent } from '../delete/medio-pago-delete-dialog.component';

@Component({
  selector: 'jhi-medio-pago',
  templateUrl: './medio-pago.component.html',
})
export class MedioPagoComponent implements OnInit {
  medioPagos?: IMedioPago[];
  isLoading = false;

  constructor(protected medioPagoService: MedioPagoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.medioPagoService.query().subscribe({
      next: (res: HttpResponse<IMedioPago[]>) => {
        this.isLoading = false;
        this.medioPagos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMedioPago): number {
    return item.id!;
  }

  delete(medioPago: IMedioPago): void {
    const modalRef = this.modalService.open(MedioPagoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.medioPago = medioPago;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
