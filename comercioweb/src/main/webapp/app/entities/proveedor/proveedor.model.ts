export interface IProveedor {
  id?: number;
  nombre?: string;
  codigo?: string;
}

export class Proveedor implements IProveedor {
  constructor(public id?: number, public nombre?: string, public codigo?: string) {}
}

export function getProveedorIdentifier(proveedor: IProveedor): number | undefined {
  return proveedor.id;
}
