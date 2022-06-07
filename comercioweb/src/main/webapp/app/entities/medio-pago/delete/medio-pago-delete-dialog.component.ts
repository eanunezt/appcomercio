import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedioPago } from '../medio-pago.model';
import { MedioPagoService } from '../service/medio-pago.service';

@Component({
  templateUrl: './medio-pago-delete-dialog.component.html',
})
export class MedioPagoDeleteDialogComponent {
  medioPago?: IMedioPago;

  constructor(protected medioPagoService: MedioPagoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medioPagoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
