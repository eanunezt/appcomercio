export interface IMedioPago {
  id?: number;
  nombre?: string;
  codigo?: string;
}

export class MedioPago implements IMedioPago {
  constructor(public id?: number, public nombre?: string, public codigo?: string) {}
}

export function getMedioPagoIdentifier(medioPago: IMedioPago): number | undefined {
  return medioPago.id;
}
