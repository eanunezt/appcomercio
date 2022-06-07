import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemOrden } from '../item-orden.model';

@Component({
  selector: 'jhi-item-orden-detail',
  templateUrl: './item-orden-detail.component.html',
})
export class ItemOrdenDetailComponent implements OnInit {
  itemOrden: IItemOrden | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemOrden }) => {
      this.itemOrden = itemOrden;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
