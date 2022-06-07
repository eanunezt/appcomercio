import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransportadorDetailComponent } from './transportador-detail.component';

describe('Transportador Management Detail Component', () => {
  let comp: TransportadorDetailComponent;
  let fixture: ComponentFixture<TransportadorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransportadorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transportador: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransportadorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransportadorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transportador on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transportador).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
