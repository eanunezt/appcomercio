import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrden, Orden } from '../orden.model';
import { OrdenService } from '../service/orden.service';

@Injectable({ providedIn: 'root' })
export class OrdenRoutingResolveService implements Resolve<IOrden> {
  constructor(protected service: OrdenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrden> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orden: HttpResponse<Orden>) => {
          if (orden.body) {
            return of(orden.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Orden());
  }
}
