import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { EstadoTransportador } from 'app/entities/enumerations/estado-transportador.model';
import { ITransportador, Transportador } from '../transportador.model';

import { TransportadorService } from './transportador.service';

describe('Transportador Service', () => {
  let service: TransportadorService;
  let httpMock: HttpTestingController;
  let elemDefault: ITransportador;
  let expectedResult: ITransportador | ITransportador[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransportadorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      codigo: 'AAAAAAA',
      estado: EstadoTransportador.ACTIVO,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Transportador', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Transportador()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Transportador', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          codigo: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Transportador', () => {
      const patchObject = Object.assign(
        {
          estado: 'BBBBBB',
        },
        new Transportador()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Transportador', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          codigo: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Transportador', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTransportadorToCollectionIfMissing', () => {
      it('should add a Transportador to an empty array', () => {
        const transportador: ITransportador = { id: 123 };
        expectedResult = service.addTransportadorToCollectionIfMissing([], transportador);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transportador);
      });

      it('should not add a Transportador to an array that contains it', () => {
        const transportador: ITransportador = { id: 123 };
        const transportadorCollection: ITransportador[] = [
          {
            ...transportador,
          },
          { id: 456 },
        ];
        expectedResult = service.addTransportadorToCollectionIfMissing(transportadorCollection, transportador);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Transportador to an array that doesn't contain it", () => {
        const transportador: ITransportador = { id: 123 };
        const transportadorCollection: ITransportador[] = [{ id: 456 }];
        expectedResult = service.addTransportadorToCollectionIfMissing(transportadorCollection, transportador);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transportador);
      });

      it('should add only unique Transportador to an array', () => {
        const transportadorArray: ITransportador[] = [{ id: 123 }, { id: 456 }, { id: 20647 }];
        const transportadorCollection: ITransportador[] = [{ id: 123 }];
        expectedResult = service.addTransportadorToCollectionIfMissing(transportadorCollection, ...transportadorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transportador: ITransportador = { id: 123 };
        const transportador2: ITransportador = { id: 456 };
        expectedResult = service.addTransportadorToCollectionIfMissing([], transportador, transportador2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transportador);
        expect(expectedResult).toContain(transportador2);
      });

      it('should accept null and undefined values', () => {
        const transportador: ITransportador = { id: 123 };
        expectedResult = service.addTransportadorToCollectionIfMissing([], null, transportador, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transportador);
      });

      it('should return initial array if no Transportador is added', () => {
        const transportadorCollection: ITransportador[] = [{ id: 123 }];
        expectedResult = service.addTransportadorToCollectionIfMissing(transportadorCollection, undefined, null);
        expect(expectedResult).toEqual(transportadorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
