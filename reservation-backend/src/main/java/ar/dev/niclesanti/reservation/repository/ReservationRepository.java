package ar.dev.niclesanti.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.dev.niclesanti.reservation.model.entity.Reservation;
import ar.dev.niclesanti.reservation.model.entity.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByDateAndTimeAndStatus(LocalDate date, LocalTime time, ReservationStatus status);
}
