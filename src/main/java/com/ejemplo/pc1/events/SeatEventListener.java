package com.ejemplo.pc1.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class SeatEventListener {

    private final AtomicLong confirmedSeats = new AtomicLong();

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotification(SeatConfirmedEvent event) {
        log.info("Notificacion: {} reservo un asiento en el viaje {} del conductor {}",
                event.passengerUsername(), event.tripId(), event.driverUsername());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerMetrics(SeatConfirmedEvent event) {
        long total = confirmedSeats.incrementAndGet();
        log.info("Metricas: asientos confirmados = {}, asientos libres en viaje {} = {}",
                total, event.tripId(), event.availableSeats());
    }

    public long getConfirmedSeats() {
        return confirmedSeats.get();
    }
}
