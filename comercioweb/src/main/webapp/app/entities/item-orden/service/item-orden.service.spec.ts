import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItemOrden, ItemOrden } from '../item-orden.model';

import { ItemOrdenService } from './item-orden.service';

describe('ItemOrden Service', () => {
  let service: ItemOrdenService;
  let httpMock: HttpTestingController;
  let elemDefault: IItemOrden;
  let expectedResult: IItemOrden | IItemOrden[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ItemOrdenService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      item: 0,
      cantidad: 0,
      valorUnidad: 0,
      valor: 0,
      codProducto: 'AAAAAAA',
      nomProdcuto: 'AAAAAAA',
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

    it('should create a ItemOrden', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ItemOrden()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemOrden', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          item: 1,
          cantidad: 1,
          valorUnidad: 1,
          valor: 1,
          codProducto: 'BBBBBB',
          nomProdcuto: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ItemOrden', () => {
      const patchObject = Object.assign(
        {
          cantidad: 1,
          valor: 1,
        },
        new ItemOrden()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemOrden', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          item: 1,
          cantidad: 1,
          valorUnidad: 1,
          valor: 1,
          codProducto: 'BBBBBB',
          nomProdcuto: 'BBBBBB',
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

    it('should delete a ItemOrden', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addItemOrdenToCollectionIfMissing', () => {
      it('should add a ItemOrden to an empty array', () => {
        const itemOrden: IItemOrden = { id: 123 };
        expectedResult = service.addItemOrdenToCollectionIfMissing([], itemOrden);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemOrden);
      });

      it('should not add a ItemOrden to an array that contains it', () => {
        const itemOrden: IItemOrden = { id: 123 };
        const itemOrdenCollection: IItemOrden[] = [
          {
            ...itemOrden,
          },
          { id: 456 },
        ];
        expectedResult = service.addItemOrdenToCollectionIfMissing(itemOrdenCollection, itemOrden);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemOrden to an array that doesn't contain it", () => {
        const itemOrden: IItemOrden = { id: 123 };
        const itemOrdenCollection: IItemOrden[] = [{ id: 456 }];
        expectedResult = service.addItemOrdenToCollectionIfMissing(itemOrdenCollection, itemOrden);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemOrden);
      });

      it('should add only unique ItemOrden to an array', () => {
        const itemOrdenArray: IItemOrden[] = [{ id: 123 }, { id: 456 }, { id: 12359 }];
        const itemOrdenCollection: IItemOrden[] = [{ id: 123 }];
        expectedResult = service.addItemOrdenToCollectionIfMissing(itemOrdenCollection, ...itemOrdenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemOrden: IItemOrden = { id: 123 };
        const itemOrden2: IItemOrden = { id: 456 };
        expectedResult = service.addItemOrdenToCollectionIfMissing([], itemOrden, itemOrden2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemOrden);
        expect(expectedResult).toContain(itemOrden2);
      });

      it('should accept null and undefined values', () => {
        const itemOrden: IItemOrden = { id: 123 };
        expectedResult = service.addItemOrdenToCollectionIfMissing([], null, itemOrden, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemOrden);
      });

      it('should return initial array if no ItemOrden is added', () => {
        const itemOrdenCollection: IItemOrden[] = [{ id: 123 }];
        expectedResult = service.addItemOrdenToCollectionIfMissing(itemOrdenCollection, undefined, null);
        expect(expectedResult).toEqual(itemOrdenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
