package in.strikes.bookmyshow.repository;

import in.strikes.bookmyshow.entity.Booking;
import in.strikes.bookmyshow.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByUser_Email(String email, Pageable pageable);
    List<Booking> findByStatusAndExpiresAtBefore(BookingStatus status, LocalDateTime now);
}
