import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMedioPago, MedioPago } from '../medio-pago.model';

import { MedioPagoService } from './medio-pago.service';

describe('MedioPago Service', () => {
  let service: MedioPagoService;
  let httpMock: HttpTestingController;
  let elemDefault: IMedioPago;
  let expectedResult: IMedioPago | IMedioPago[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MedioPagoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      codigo: 'AAAAAAA',
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

    it('should create a MedioPago', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MedioPago()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MedioPago', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          codigo: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MedioPago', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
        },
        new MedioPago()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MedioPago', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          codigo: 'BBBBBB',
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

    it('should delete a MedioPago', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMedioPagoToCollectionIfMissing', () => {
      it('should add a MedioPago to an empty array', () => {
        const medioPago: IMedioPago = { id: 123 };
        expectedResult = service.addMedioPagoToCollectionIfMissing([], medioPago);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medioPago);
      });

      it('should not add a MedioPago to an array that contains it', () => {
        const medioPago: IMedioPago = { id: 123 };
        const medioPagoCollection: IMedioPago[] = [
          {
            ...medioPago,
          },
          { id: 456 },
        ];
        expectedResult = service.addMedioPagoToCollectionIfMissing(medioPagoCollection, medioPago);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MedioPago to an array that doesn't contain it", () => {
        const medioPago: IMedioPago = { id: 123 };
        const medioPagoCollection: IMedioPago[] = [{ id: 456 }];
        expectedResult = service.addMedioPagoToCollectionIfMissing(medioPagoCollection, medioPago);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medioPago);
      });

      it('should add only unique MedioPago to an array', () => {
        const medioPagoArray: IMedioPago[] = [{ id: 123 }, { id: 456 }, { id: 12465 }];
        const medioPagoCollection: IMedioPago[] = [{ id: 123 }];
        expectedResult = service.addMedioPagoToCollectionIfMissing(medioPagoCollection, ...medioPagoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medioPago: IMedioPago = { id: 123 };
        const medioPago2: IMedioPago = { id: 456 };
        expectedResult = service.addMedioPagoToCollectionIfMissing([], medioPago, medioPago2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medioPago);
        expect(expectedResult).toContain(medioPago2);
      });

      it('should accept null and undefined values', () => {
        const medioPago: IMedioPago = { id: 123 };
        expectedResult = service.addMedioPagoToCollectionIfMissing([], null, medioPago, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medioPago);
      });

      it('should return initial array if no MedioPago is added', () => {
        const medioPagoCollection: IMedioPago[] = [{ id: 123 }];
        expectedResult = service.addMedioPagoToCollectionIfMissing(medioPagoCollection, undefined, null);
        expect(expectedResult).toEqual(medioPagoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
