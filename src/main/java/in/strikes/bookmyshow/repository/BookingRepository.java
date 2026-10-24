package in.strikes.bookmyshow.repository;

import in.strikes.bookmyshow.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByUser_Email(String email, Pageable pageable);
}
