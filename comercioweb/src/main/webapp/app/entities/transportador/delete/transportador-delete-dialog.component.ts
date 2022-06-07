import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransportador } from '../transportador.model';
import { TransportadorService } from '../service/transportador.service';

@Component({
  templateUrl: './transportador-delete-dialog.component.html',
})
export class TransportadorDeleteDialogComponent {
  transportador?: ITransportador;

  constructor(protected transportadorService: TransportadorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transportadorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
