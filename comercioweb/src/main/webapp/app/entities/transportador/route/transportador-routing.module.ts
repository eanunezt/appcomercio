import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransportadorComponent } from '../list/transportador.component';
import { TransportadorDetailComponent } from '../detail/transportador-detail.component';
import { TransportadorUpdateComponent } from '../update/transportador-update.component';
import { TransportadorRoutingResolveService } from './transportador-routing-resolve.service';

const transportadorRoute: Routes = [
  {
    path: '',
    component: TransportadorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransportadorDetailComponent,
    resolve: {
      transportador: TransportadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransportadorUpdateComponent,
    resolve: {
      transportador: TransportadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransportadorUpdateComponent,
    resolve: {
      transportador: TransportadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transportadorRoute)],
  exports: [RouterModule],
})
export class TransportadorRoutingModule {}
