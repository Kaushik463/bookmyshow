package in.strikes.bookmyshow.repository;

import in.strikes.bookmyshow.entity.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByShowId(Long showId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ss from ShowSeat ss where ss.show.id = :showId and ss.seat.id in :seatIds")
    List<ShowSeat> findAndLockByShowIdAndSeatIds(Long showId, List<Long> seatIds);

    @Modifying
    @Query("update ShowSeat ss set ss.status = 'AVAILABLE', ss.booking = null, ss.lockedUntil = null " +
            "where ss.status = 'LOCKED' and ss.lockedUntil < :now")
    int releaseExpiredLocks(LocalDateTime now);
}
