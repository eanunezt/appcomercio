import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ItemOrdenComponent } from './list/item-orden.component';
import { ItemOrdenDetailComponent } from './detail/item-orden-detail.component';
import { ItemOrdenUpdateComponent } from './update/item-orden-update.component';
import { ItemOrdenDeleteDialogComponent } from './delete/item-orden-delete-dialog.component';
import { ItemOrdenRoutingModule } from './route/item-orden-routing.module';

@NgModule({
  imports: [SharedModule, ItemOrdenRoutingModule],
  declarations: [ItemOrdenComponent, ItemOrdenDetailComponent, ItemOrdenUpdateComponent, ItemOrdenDeleteDialogComponent],
  entryComponents: [ItemOrdenDeleteDialogComponent],
})
export class ItemOrdenModule {}
