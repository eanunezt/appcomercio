import dayjs from 'dayjs/esm';
import { IMedioPago } from 'app/entities/medio-pago/medio-pago.model';
import { ITransportador } from 'app/entities/transportador/transportador.model';
import { IItemOrden } from 'app/entities/item-orden/item-orden.model';
import { EstadoOrden } from 'app/entities/enumerations/estado-orden.model';

export interface IOrden {
  id?: number;
  fechaRegistro?: dayjs.Dayjs;
  fechaEntregaEstimada?: dayjs.Dayjs;
  fechaEnntregaReal?: dayjs.Dayjs;
  descripcion?: string;
  codigo?: string;
  valorTotal?: number;
  factura?: string;
  estado?: EstadoOrden;
  codCliente?: string;
  datosCliente?: string;
  mediPago?: IMedioPago | null;
  transportador?: ITransportador | null;
  items?: IItemOrden[] | null;
}

export class Orden implements IOrden {
  constructor(
    public id?: number,
    public fechaRegistro?: dayjs.Dayjs,
    public fechaEntregaEstimada?: dayjs.Dayjs,
    public fechaEnntregaReal?: dayjs.Dayjs,
    public descripcion?: string,
    public codigo?: string,
    public valorTotal?: number,
    public factura?: string,
    public estado?: EstadoOrden,
    public codCliente?: string,
    public datosCliente?: string,
    public mediPago?: IMedioPago | null,
    public transportador?: ITransportador | null,
    public items?: IItemOrden[] | null
  ) {}
}

export function getOrdenIdentifier(orden: IOrden): number | undefined {
  return orden.id;
}
