import { IDireccion } from 'app/entities/direccion/direccion.model';
import { TipoDocumento } from 'app/entities/enumerations/tipo-documento.model';

export interface ICliente {
  id?: number;
  nombre?: string;
  tipoDcoumento?: TipoDocumento;
  numDocumento?: string;
  email?: string;
  numTelefono?: string;
  direcciones?: IDireccion[] | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string,
    public tipoDcoumento?: TipoDocumento,
    public numDocumento?: string,
    public email?: string,
    public numTelefono?: string,
    public direcciones?: IDireccion[] | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
