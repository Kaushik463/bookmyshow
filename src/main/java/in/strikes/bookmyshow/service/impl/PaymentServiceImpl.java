package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.dto.request.PaymentRequest;
import in.strikes.bookmyshow.entity.Booking;
import in.strikes.bookmyshow.entity.Payment;
import in.strikes.bookmyshow.enums.PaymentStatus;
import in.strikes.bookmyshow.exception.PaymentFailedException;
import in.strikes.bookmyshow.repository.PaymentRepository;
import in.strikes.bookmyshow.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Booking booking, PaymentRequest paymentRequest) {
        // Mock gateway: simulate success. Swap this for a real gateway integration later.
        boolean success = true;

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .status(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .transactionRef(UUID.randomUUID().toString())
                .paidAt(success ? LocalDateTime.now() : null)
                .build();

        paymentRepository.save(payment);

        if (!success) {
            throw new PaymentFailedException("Payment failed for booking: " + booking.getId());
        }
    }
}
