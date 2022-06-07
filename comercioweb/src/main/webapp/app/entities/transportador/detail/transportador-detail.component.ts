import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransportador } from '../transportador.model';

@Component({
  selector: 'jhi-transportador-detail',
  templateUrl: './transportador-detail.component.html',
})
export class TransportadorDetailComponent implements OnInit {
  transportador: ITransportador | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transportador }) => {
      this.transportador = transportador;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
