import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemOrdenDetailComponent } from './item-orden-detail.component';

describe('ItemOrden Management Detail Component', () => {
  let comp: ItemOrdenDetailComponent;
  let fixture: ComponentFixture<ItemOrdenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemOrdenDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ itemOrden: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ItemOrdenDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ItemOrdenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load itemOrden on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.itemOrden).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
