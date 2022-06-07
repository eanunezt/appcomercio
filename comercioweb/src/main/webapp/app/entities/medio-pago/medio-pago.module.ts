import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MedioPagoComponent } from './list/medio-pago.component';
import { MedioPagoDetailComponent } from './detail/medio-pago-detail.component';
import { MedioPagoUpdateComponent } from './update/medio-pago-update.component';
import { MedioPagoDeleteDialogComponent } from './delete/medio-pago-delete-dialog.component';
import { MedioPagoRoutingModule } from './route/medio-pago-routing.module';

@NgModule({
  imports: [SharedModule, MedioPagoRoutingModule],
  declarations: [MedioPagoComponent, MedioPagoDetailComponent, MedioPagoUpdateComponent, MedioPagoDeleteDialogComponent],
  entryComponents: [MedioPagoDeleteDialogComponent],
})
export class MedioPagoModule {}
