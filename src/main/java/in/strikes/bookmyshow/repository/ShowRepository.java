package in.strikes.bookmyshow.repository;

import in.strikes.bookmyshow.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show,Long> {
    List<Show> findByMovieIdAndStartTimeBetween(Long movieId, LocalDateTime start, LocalDateTime end);
}
