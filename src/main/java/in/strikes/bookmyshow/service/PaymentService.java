package in.strikes.bookmyshow.service;

import in.strikes.bookmyshow.dto.request.PaymentRequest;
import in.strikes.bookmyshow.entity.Booking;

public interface PaymentService {
    void processPayment(Booking booking, PaymentRequest paymentRequest);
}
