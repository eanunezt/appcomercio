import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IDireccion {
  id?: number;
  direccion?: string;
  ciudad?: string;
  barrio?: string;
  cliente?: ICliente | null;
}

export class Direccion implements IDireccion {
  constructor(
    public id?: number,
    public direccion?: string,
    public ciudad?: string,
    public barrio?: string,
    public cliente?: ICliente | null
  ) {}
}

export function getDireccionIdentifier(direccion: IDireccion): number | undefined {
  return direccion.id;
}
