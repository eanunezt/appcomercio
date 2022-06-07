import { EstadoTransportador } from 'app/entities/enumerations/estado-transportador.model';

export interface ITransportador {
  id?: number;
  nombre?: string;
  codigo?: string;
  estado?: EstadoTransportador;
}

export class Transportador implements ITransportador {
  constructor(public id?: number, public nombre?: string, public codigo?: string, public estado?: EstadoTransportador) {}
}

export function getTransportadorIdentifier(transportador: ITransportador): number | undefined {
  return transportador.id;
}
