import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrden, getOrdenIdentifier } from '../orden.model';

export type EntityResponseType = HttpResponse<IOrden>;
export type EntityArrayResponseType = HttpResponse<IOrden[]>;

@Injectable({ providedIn: 'root' })
export class OrdenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ordens');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orden: IOrden): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orden);
    return this.http
      .post<IOrden>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(orden: IOrden): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orden);
    return this.http
      .put<IOrden>(`${this.resourceUrl}/${getOrdenIdentifier(orden) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(orden: IOrden): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orden);
    return this.http
      .patch<IOrden>(`${this.resourceUrl}/${getOrdenIdentifier(orden) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrden>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrden[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrdenToCollectionIfMissing(ordenCollection: IOrden[], ...ordensToCheck: (IOrden | null | undefined)[]): IOrden[] {
    const ordens: IOrden[] = ordensToCheck.filter(isPresent);
    if (ordens.length > 0) {
      const ordenCollectionIdentifiers = ordenCollection.map(ordenItem => getOrdenIdentifier(ordenItem)!);
      const ordensToAdd = ordens.filter(ordenItem => {
        const ordenIdentifier = getOrdenIdentifier(ordenItem);
        if (ordenIdentifier == null || ordenCollectionIdentifiers.includes(ordenIdentifier)) {
          return false;
        }
        ordenCollectionIdentifiers.push(ordenIdentifier);
        return true;
      });
      return [...ordensToAdd, ...ordenCollection];
    }
    return ordenCollection;
  }

  protected convertDateFromClient(orden: IOrden): IOrden {
    return Object.assign({}, orden, {
      fechaRegistro: orden.fechaRegistro?.isValid() ? orden.fechaRegistro.toJSON() : undefined,
      fechaEntregaEstimada: orden.fechaEntregaEstimada?.isValid() ? orden.fechaEntregaEstimada.toJSON() : undefined,
      fechaEnntregaReal: orden.fechaEnntregaReal?.isValid() ? orden.fechaEnntregaReal.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaRegistro = res.body.fechaRegistro ? dayjs(res.body.fechaRegistro) : undefined;
      res.body.fechaEntregaEstimada = res.body.fechaEntregaEstimada ? dayjs(res.body.fechaEntregaEstimada) : undefined;
      res.body.fechaEnntregaReal = res.body.fechaEnntregaReal ? dayjs(res.body.fechaEnntregaReal) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((orden: IOrden) => {
        orden.fechaRegistro = orden.fechaRegistro ? dayjs(orden.fechaRegistro) : undefined;
        orden.fechaEntregaEstimada = orden.fechaEntregaEstimada ? dayjs(orden.fechaEntregaEstimada) : undefined;
        orden.fechaEnntregaReal = orden.fechaEnntregaReal ? dayjs(orden.fechaEnntregaReal) : undefined;
      });
    }
    return res;
  }
}
