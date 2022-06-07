import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemOrden, getItemOrdenIdentifier } from '../item-orden.model';

export type EntityResponseType = HttpResponse<IItemOrden>;
export type EntityArrayResponseType = HttpResponse<IItemOrden[]>;

@Injectable({ providedIn: 'root' })
export class ItemOrdenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/item-ordens');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(itemOrden: IItemOrden): Observable<EntityResponseType> {
    return this.http.post<IItemOrden>(this.resourceUrl, itemOrden, { observe: 'response' });
  }

  update(itemOrden: IItemOrden): Observable<EntityResponseType> {
    return this.http.put<IItemOrden>(`${this.resourceUrl}/${getItemOrdenIdentifier(itemOrden) as number}`, itemOrden, {
      observe: 'response',
    });
  }

  partialUpdate(itemOrden: IItemOrden): Observable<EntityResponseType> {
    return this.http.patch<IItemOrden>(`${this.resourceUrl}/${getItemOrdenIdentifier(itemOrden) as number}`, itemOrden, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IItemOrden>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItemOrden[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemOrdenToCollectionIfMissing(
    itemOrdenCollection: IItemOrden[],
    ...itemOrdensToCheck: (IItemOrden | null | undefined)[]
  ): IItemOrden[] {
    const itemOrdens: IItemOrden[] = itemOrdensToCheck.filter(isPresent);
    if (itemOrdens.length > 0) {
      const itemOrdenCollectionIdentifiers = itemOrdenCollection.map(itemOrdenItem => getItemOrdenIdentifier(itemOrdenItem)!);
      const itemOrdensToAdd = itemOrdens.filter(itemOrdenItem => {
        const itemOrdenIdentifier = getItemOrdenIdentifier(itemOrdenItem);
        if (itemOrdenIdentifier == null || itemOrdenCollectionIdentifiers.includes(itemOrdenIdentifier)) {
          return false;
        }
        itemOrdenCollectionIdentifiers.push(itemOrdenIdentifier);
        return true;
      });
      return [...itemOrdensToAdd, ...itemOrdenCollection];
    }
    return itemOrdenCollection;
  }
}
