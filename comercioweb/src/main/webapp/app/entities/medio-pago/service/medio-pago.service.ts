import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedioPago, getMedioPagoIdentifier } from '../medio-pago.model';

export type EntityResponseType = HttpResponse<IMedioPago>;
export type EntityArrayResponseType = HttpResponse<IMedioPago[]>;

@Injectable({ providedIn: 'root' })
export class MedioPagoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medio-pagos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(medioPago: IMedioPago): Observable<EntityResponseType> {
    return this.http.post<IMedioPago>(this.resourceUrl, medioPago, { observe: 'response' });
  }

  update(medioPago: IMedioPago): Observable<EntityResponseType> {
    return this.http.put<IMedioPago>(`${this.resourceUrl}/${getMedioPagoIdentifier(medioPago) as number}`, medioPago, {
      observe: 'response',
    });
  }

  partialUpdate(medioPago: IMedioPago): Observable<EntityResponseType> {
    return this.http.patch<IMedioPago>(`${this.resourceUrl}/${getMedioPagoIdentifier(medioPago) as number}`, medioPago, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedioPago>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedioPago[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMedioPagoToCollectionIfMissing(
    medioPagoCollection: IMedioPago[],
    ...medioPagosToCheck: (IMedioPago | null | undefined)[]
  ): IMedioPago[] {
    const medioPagos: IMedioPago[] = medioPagosToCheck.filter(isPresent);
    if (medioPagos.length > 0) {
      const medioPagoCollectionIdentifiers = medioPagoCollection.map(medioPagoItem => getMedioPagoIdentifier(medioPagoItem)!);
      const medioPagosToAdd = medioPagos.filter(medioPagoItem => {
        const medioPagoIdentifier = getMedioPagoIdentifier(medioPagoItem);
        if (medioPagoIdentifier == null || medioPagoCollectionIdentifiers.includes(medioPagoIdentifier)) {
          return false;
        }
        medioPagoCollectionIdentifiers.push(medioPagoIdentifier);
        return true;
      });
      return [...medioPagosToAdd, ...medioPagoCollection];
    }
    return medioPagoCollection;
  }
}
