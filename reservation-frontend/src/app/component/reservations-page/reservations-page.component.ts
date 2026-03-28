import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { finalize } from 'rxjs';
import { Reservation, ReservationStatus } from '../../model/reservation.model';
import { ReservationService } from '../../service/reservation.service';
import { ToastComponent } from '../toast/toast.component';

@Component({
  selector: 'app-reservations-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ToastComponent],
  templateUrl: './reservations-page.component.html',
  styleUrl: './reservations-page.component.css'
})
export class ReservationsPageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);

  reservations: Reservation[] = [];
  readonly serviceOptions = [
    'Corte de cabello',
    'Coloracion',
    'Masaje descontracturante',
    'Manicura'
  ];

  readonly form = this.formBuilder.nonNullable.group({
    nombreCliente: ['', Validators.required],
    fecha: ['', Validators.required],
    hora: ['', Validators.required],
    servicio: ['', Validators.required]
  });

  isLoading = false;
  isCreating = false;
  errorMessage = '';
  toastMessage = '';

  constructor(private readonly reservationService: ReservationService) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.reservationService
      .list()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (reservations) => {
          this.reservations = reservations;
        },
        error: () => {
          this.errorMessage = 'No se pudieron cargar las reservas. Intenta nuevamente.';
        }
      });
  }

  submitReservation(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formValue = this.form.getRawValue();

    this.isCreating = true;
    this.toastMessage = '';

    this.reservationService
      .create({
        customerName: formValue.nombreCliente,
        date: formValue.fecha,
        time: formValue.hora,
        service: formValue.servicio
      })
      .pipe(finalize(() => (this.isCreating = false)))
      .subscribe({
        next: (reservation) => {
          this.reservations = [reservation, ...this.reservations];
          this.form.reset();
        },
        error: (error: HttpErrorResponse) => {
          this.toastMessage =
            error.error?.message ?? 'No se pudo guardar la reserva. Intenta nuevamente.';
        }
      });
  }

  hideToast(): void {
    this.toastMessage = '';
  }

  cancel(id: number): void {
    if (!window.confirm('¿Seguro que quieres cancelar esta reserva?')) {
      return;
    }

    this.reservationService.cancel(id).subscribe({
      next: () => {
        this.reservations = this.reservations.map((reservation) =>
          reservation.id === id
            ? { ...reservation, status: ReservationStatus.CANCELLED }
            : reservation
        );
      },
      error: () => {
        this.errorMessage = 'No se pudo cancelar la reserva. Intenta nuevamente.';
      }
    });
  }

  isFieldInvalid(fieldName: 'nombreCliente' | 'fecha' | 'hora' | 'servicio'): boolean {
    const field = this.form.controls[fieldName];
    return field.invalid && (field.dirty || field.touched);
  }
}
