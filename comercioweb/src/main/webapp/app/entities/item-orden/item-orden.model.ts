import { IOrden } from 'app/entities/orden/orden.model';

export interface IItemOrden {
  id?: number;
  item?: number;
  cantidad?: number;
  valorUnidad?: number;
  valor?: number;
  codProducto?: string;
  nomProdcuto?: string;
  orden?: IOrden | null;
}

export class ItemOrden implements IItemOrden {
  constructor(
    public id?: number,
    public item?: number,
    public cantidad?: number,
    public valorUnidad?: number,
    public valor?: number,
    public codProducto?: string,
    public nomProdcuto?: string,
    public orden?: IOrden | null
  ) {}
}

export function getItemOrdenIdentifier(itemOrden: IItemOrden): number | undefined {
  return itemOrden.id;
}
