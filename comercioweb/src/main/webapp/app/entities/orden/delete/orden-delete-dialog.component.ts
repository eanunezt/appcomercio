import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrden } from '../orden.model';
import { OrdenService } from '../service/orden.service';

@Component({
  templateUrl: './orden-delete-dialog.component.html',
})
export class OrdenDeleteDialogComponent {
  orden?: IOrden;

  constructor(protected ordenService: OrdenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
