export interface ITipoProducto {
  id?: number;
  nombre?: string;
  codigo?: string;
}

export class TipoProducto implements ITipoProducto {
  constructor(public id?: number, public nombre?: string, public codigo?: string) {}
}

export function getTipoProductoIdentifier(tipoProducto: ITipoProducto): number | undefined {
  return tipoProducto.id;
}
