import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransportador, getTransportadorIdentifier } from '../transportador.model';

export type EntityResponseType = HttpResponse<ITransportador>;
export type EntityArrayResponseType = HttpResponse<ITransportador[]>;

@Injectable({ providedIn: 'root' })
export class TransportadorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transportadors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transportador: ITransportador): Observable<EntityResponseType> {
    return this.http.post<ITransportador>(this.resourceUrl, transportador, { observe: 'response' });
  }

  update(transportador: ITransportador): Observable<EntityResponseType> {
    return this.http.put<ITransportador>(`${this.resourceUrl}/${getTransportadorIdentifier(transportador) as number}`, transportador, {
      observe: 'response',
    });
  }

  partialUpdate(transportador: ITransportador): Observable<EntityResponseType> {
    return this.http.patch<ITransportador>(`${this.resourceUrl}/${getTransportadorIdentifier(transportador) as number}`, transportador, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransportador>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransportador[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTransportadorToCollectionIfMissing(
    transportadorCollection: ITransportador[],
    ...transportadorsToCheck: (ITransportador | null | undefined)[]
  ): ITransportador[] {
    const transportadors: ITransportador[] = transportadorsToCheck.filter(isPresent);
    if (transportadors.length > 0) {
      const transportadorCollectionIdentifiers = transportadorCollection.map(
        transportadorItem => getTransportadorIdentifier(transportadorItem)!
      );
      const transportadorsToAdd = transportadors.filter(transportadorItem => {
        const transportadorIdentifier = getTransportadorIdentifier(transportadorItem);
        if (transportadorIdentifier == null || transportadorCollectionIdentifiers.includes(transportadorIdentifier)) {
          return false;
        }
        transportadorCollectionIdentifiers.push(transportadorIdentifier);
        return true;
      });
      return [...transportadorsToAdd, ...transportadorCollection];
    }
    return transportadorCollection;
  }
}
