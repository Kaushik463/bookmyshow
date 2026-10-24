package in.strikes.bookmyshow.repository;

import in.strikes.bookmyshow.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByActiveTrueAndGenreContainingIgnoreCase(String genre, Pageable pageable);
    Page<Movie> findByActiveTrue(Pageable pageable);
}
