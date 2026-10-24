package in.strikes.bookmyshow.service;

import in.strikes.bookmyshow.dto.request.MovieRequest;
import in.strikes.bookmyshow.dto.response.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    MovieResponse create(MovieRequest request);
    MovieResponse update(Long id, MovieRequest request);
    void delete(Long id);
    MovieResponse getById(Long id);
    Page<MovieResponse> search(String genre, Pageable pageable);
}
