package ar.dev.niclesanti.reservation.controller;

import ar.dev.niclesanti.reservation.model.entity.Reservation;
import ar.dev.niclesanti.reservation.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> list() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        Reservation created = reservationService.create(reservation);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(created.getId())
                                .toUri())
                .body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        reservationService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
