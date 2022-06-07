import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransportador, Transportador } from '../transportador.model';
import { TransportadorService } from '../service/transportador.service';

@Injectable({ providedIn: 'root' })
export class TransportadorRoutingResolveService implements Resolve<ITransportador> {
  constructor(protected service: TransportadorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransportador> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transportador: HttpResponse<Transportador>) => {
          if (transportador.body) {
            return of(transportador.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Transportador());
  }
}
