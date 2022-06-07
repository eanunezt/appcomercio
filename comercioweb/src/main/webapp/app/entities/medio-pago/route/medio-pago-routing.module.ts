import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MedioPagoComponent } from '../list/medio-pago.component';
import { MedioPagoDetailComponent } from '../detail/medio-pago-detail.component';
import { MedioPagoUpdateComponent } from '../update/medio-pago-update.component';
import { MedioPagoRoutingResolveService } from './medio-pago-routing-resolve.service';

const medioPagoRoute: Routes = [
  {
    path: '',
    component: MedioPagoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedioPagoDetailComponent,
    resolve: {
      medioPago: MedioPagoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedioPagoUpdateComponent,
    resolve: {
      medioPago: MedioPagoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedioPagoUpdateComponent,
    resolve: {
      medioPago: MedioPagoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(medioPagoRoute)],
  exports: [RouterModule],
})
export class MedioPagoRoutingModule {}
