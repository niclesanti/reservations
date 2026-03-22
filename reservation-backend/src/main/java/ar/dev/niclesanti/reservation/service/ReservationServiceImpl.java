package ar.dev.niclesanti.reservation.service;

import ar.dev.niclesanti.reservation.exception.BusinessRuleException;
import ar.dev.niclesanti.reservation.model.entity.Reservation;
import ar.dev.niclesanti.reservation.model.entity.ReservationStatus;
import ar.dev.niclesanti.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional
    public Reservation create(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeAndStatus(
                reservation.getDate(), reservation.getTime(), ReservationStatus.ACTIVE)) {
            throw new BusinessRuleException(
                    "Ya existe una reserva activa para la misma fecha y hora.",
                    HttpStatus.CONFLICT);
        }
        reservation.setStatus(ReservationStatus.ACTIVE);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        Reservation reservation =
                reservationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new BusinessRuleException(
                                                "No existe la reserva con el id indicado.",
                                                HttpStatus.NOT_FOUND));
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessRuleException("La reserva ya está cancelada.", HttpStatus.CONFLICT);
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}
