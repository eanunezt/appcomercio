import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemOrdenComponent } from '../list/item-orden.component';
import { ItemOrdenDetailComponent } from '../detail/item-orden-detail.component';
import { ItemOrdenUpdateComponent } from '../update/item-orden-update.component';
import { ItemOrdenRoutingResolveService } from './item-orden-routing-resolve.service';

const itemOrdenRoute: Routes = [
  {
    path: '',
    component: ItemOrdenComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemOrdenDetailComponent,
    resolve: {
      itemOrden: ItemOrdenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemOrdenUpdateComponent,
    resolve: {
      itemOrden: ItemOrdenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemOrdenUpdateComponent,
    resolve: {
      itemOrden: ItemOrdenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemOrdenRoute)],
  exports: [RouterModule],
})
export class ItemOrdenRoutingModule {}
