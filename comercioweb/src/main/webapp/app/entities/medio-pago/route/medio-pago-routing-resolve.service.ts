import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedioPago, MedioPago } from '../medio-pago.model';
import { MedioPagoService } from '../service/medio-pago.service';

@Injectable({ providedIn: 'root' })
export class MedioPagoRoutingResolveService implements Resolve<IMedioPago> {
  constructor(protected service: MedioPagoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedioPago> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((medioPago: HttpResponse<MedioPago>) => {
          if (medioPago.body) {
            return of(medioPago.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MedioPago());
  }
}
