import { ITipoProducto } from 'app/entities/tipo-producto/tipo-producto.model';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { EstadoProducto } from 'app/entities/enumerations/estado-producto.model';

export interface IProducto {
  id?: number;
  nombre?: string;
  codigo?: string;
  valor?: number;
  estado?: EstadoProducto;
  tipoProducto?: ITipoProducto | null;
  proveedor?: IProveedor | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombre?: string,
    public codigo?: string,
    public valor?: number,
    public estado?: EstadoProducto,
    public tipoProducto?: ITipoProducto | null,
    public proveedor?: IProveedor | null
  ) {}
}

export function getProductoIdentifier(producto: IProducto): number | undefined {
  return producto.id;
}
