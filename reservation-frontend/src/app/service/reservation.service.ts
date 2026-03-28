import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Reservation } from '../model/reservation.model';

export type CreateReservationRequest = Omit<Reservation, 'id' | 'status'>;

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private readonly reservationUrl = `${environment.backendUrl}/reservation`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.reservationUrl);
  }

  create(reservation: CreateReservationRequest): Observable<Reservation> {
    return this.http.post<Reservation>(this.reservationUrl, reservation);
  }

  cancel(id: number): Observable<void> {
    return this.http.delete<void>(`${this.reservationUrl}/${id}`);
  }
}
