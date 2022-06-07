import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemOrden, ItemOrden } from '../item-orden.model';
import { ItemOrdenService } from '../service/item-orden.service';

@Injectable({ providedIn: 'root' })
export class ItemOrdenRoutingResolveService implements Resolve<IItemOrden> {
  constructor(protected service: ItemOrdenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemOrden> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemOrden: HttpResponse<ItemOrden>) => {
          if (itemOrden.body) {
            return of(itemOrden.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemOrden());
  }
}
