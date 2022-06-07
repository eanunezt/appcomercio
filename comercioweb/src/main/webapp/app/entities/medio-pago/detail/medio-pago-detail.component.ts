import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedioPago } from '../medio-pago.model';

@Component({
  selector: 'jhi-medio-pago-detail',
  templateUrl: './medio-pago-detail.component.html',
})
export class MedioPagoDetailComponent implements OnInit {
  medioPago: IMedioPago | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medioPago }) => {
      this.medioPago = medioPago;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
