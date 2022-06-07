import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransportadorComponent } from './list/transportador.component';
import { TransportadorDetailComponent } from './detail/transportador-detail.component';
import { TransportadorUpdateComponent } from './update/transportador-update.component';
import { TransportadorDeleteDialogComponent } from './delete/transportador-delete-dialog.component';
import { TransportadorRoutingModule } from './route/transportador-routing.module';

@NgModule({
  imports: [SharedModule, TransportadorRoutingModule],
  declarations: [TransportadorComponent, TransportadorDetailComponent, TransportadorUpdateComponent, TransportadorDeleteDialogComponent],
  entryComponents: [TransportadorDeleteDialogComponent],
})
export class TransportadorModule {}
