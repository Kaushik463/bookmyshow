package in.strikes.bookmyshow.repository;

import in.strikes.bookmyshow.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
