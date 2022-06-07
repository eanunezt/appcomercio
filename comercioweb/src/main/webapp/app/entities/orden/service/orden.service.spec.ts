import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { EstadoOrden } from 'app/entities/enumerations/estado-orden.model';
import { IOrden, Orden } from '../orden.model';

import { OrdenService } from './orden.service';

describe('Orden Service', () => {
  let service: OrdenService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrden;
  let expectedResult: IOrden | IOrden[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrdenService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaRegistro: currentDate,
      fechaEntregaEstimada: currentDate,
      fechaEnntregaReal: currentDate,
      descripcion: 'AAAAAAA',
      codigo: 'AAAAAAA',
      valorTotal: 0,
      factura: 'AAAAAAA',
      estado: EstadoOrden.ENTREGADO,
      codCliente: 'AAAAAAA',
      datosCliente: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          fechaEntregaEstimada: currentDate.format(DATE_TIME_FORMAT),
          fechaEnntregaReal: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Orden', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          fechaEntregaEstimada: currentDate.format(DATE_TIME_FORMAT),
          fechaEnntregaReal: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
          fechaEntregaEstimada: currentDate,
          fechaEnntregaReal: currentDate,
        },
        returnedFromService
      );

      service.create(new Orden()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Orden', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          fechaEntregaEstimada: currentDate.format(DATE_TIME_FORMAT),
          fechaEnntregaReal: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
          codigo: 'BBBBBB',
          valorTotal: 1,
          factura: 'BBBBBB',
          estado: 'BBBBBB',
          codCliente: 'BBBBBB',
          datosCliente: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
          fechaEntregaEstimada: currentDate,
          fechaEnntregaReal: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Orden', () => {
      const patchObject = Object.assign(
        {
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          valorTotal: 1,
          estado: 'BBBBBB',
          codCliente: 'BBBBBB',
        },
        new Orden()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
          fechaEntregaEstimada: currentDate,
          fechaEnntregaReal: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Orden', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          fechaEntregaEstimada: currentDate.format(DATE_TIME_FORMAT),
          fechaEnntregaReal: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
          codigo: 'BBBBBB',
          valorTotal: 1,
          factura: 'BBBBBB',
          estado: 'BBBBBB',
          codCliente: 'BBBBBB',
          datosCliente: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
          fechaEntregaEstimada: currentDate,
          fechaEnntregaReal: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Orden', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrdenToCollectionIfMissing', () => {
      it('should add a Orden to an empty array', () => {
        const orden: IOrden = { id: 123 };
        expectedResult = service.addOrdenToCollectionIfMissing([], orden);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orden);
      });

      it('should not add a Orden to an array that contains it', () => {
        const orden: IOrden = { id: 123 };
        const ordenCollection: IOrden[] = [
          {
            ...orden,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrdenToCollectionIfMissing(ordenCollection, orden);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Orden to an array that doesn't contain it", () => {
        const orden: IOrden = { id: 123 };
        const ordenCollection: IOrden[] = [{ id: 456 }];
        expectedResult = service.addOrdenToCollectionIfMissing(ordenCollection, orden);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orden);
      });

      it('should add only unique Orden to an array', () => {
        const ordenArray: IOrden[] = [{ id: 123 }, { id: 456 }, { id: 78253 }];
        const ordenCollection: IOrden[] = [{ id: 123 }];
        expectedResult = service.addOrdenToCollectionIfMissing(ordenCollection, ...ordenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orden: IOrden = { id: 123 };
        const orden2: IOrden = { id: 456 };
        expectedResult = service.addOrdenToCollectionIfMissing([], orden, orden2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orden);
        expect(expectedResult).toContain(orden2);
      });

      it('should accept null and undefined values', () => {
        const orden: IOrden = { id: 123 };
        expectedResult = service.addOrdenToCollectionIfMissing([], null, orden, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orden);
      });

      it('should return initial array if no Orden is added', () => {
        const ordenCollection: IOrden[] = [{ id: 123 }];
        expectedResult = service.addOrdenToCollectionIfMissing(ordenCollection, undefined, null);
        expect(expectedResult).toEqual(ordenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
