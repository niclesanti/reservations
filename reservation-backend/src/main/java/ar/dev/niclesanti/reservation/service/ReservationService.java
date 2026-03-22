package ar.dev.niclesanti.reservation.service;

import ar.dev.niclesanti.reservation.model.entity.Reservation;
import java.util.List;

public interface ReservationService {

    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void cancel(Long id);
}
