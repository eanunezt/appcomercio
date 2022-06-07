import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemOrden } from '../item-orden.model';
import { ItemOrdenService } from '../service/item-orden.service';

@Component({
  templateUrl: './item-orden-delete-dialog.component.html',
})
export class ItemOrdenDeleteDialogComponent {
  itemOrden?: IItemOrden;

  constructor(protected itemOrdenService: ItemOrdenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemOrdenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
