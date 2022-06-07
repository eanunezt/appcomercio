import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IItemOrden, ItemOrden } from '../item-orden.model';
import { ItemOrdenService } from '../service/item-orden.service';

import { ItemOrdenRoutingResolveService } from './item-orden-routing-resolve.service';

describe('ItemOrden routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ItemOrdenRoutingResolveService;
  let service: ItemOrdenService;
  let resultItemOrden: IItemOrden | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ItemOrdenRoutingResolveService);
    service = TestBed.inject(ItemOrdenService);
    resultItemOrden = undefined;
  });

  describe('resolve', () => {
    it('should return IItemOrden returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemOrden = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultItemOrden).toEqual({ id: 123 });
    });

    it('should return new IItemOrden if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemOrden = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultItemOrden).toEqual(new ItemOrden());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ItemOrden })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemOrden = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultItemOrden).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
