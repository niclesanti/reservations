export enum ReservationStatus {
  ACTIVE = 'ACTIVE',
  CANCELLED = 'CANCELLED'
}

export interface Reservation {
  id: number;
  customerName: string;
  date: string;
  time: string;
  service: string;
  status: ReservationStatus;
}