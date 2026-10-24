package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.dto.request.MovieRequest;
import in.strikes.bookmyshow.dto.response.MovieResponse;
import in.strikes.bookmyshow.entity.Movie;
import in.strikes.bookmyshow.exception.ResourceNotFoundException;
import in.strikes.bookmyshow.mapper.MovieMapper;
import in.strikes.bookmyshow.repository.MovieRepository;
import in.strikes.bookmyshow.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public MovieResponse create(MovieRequest request) {
        Movie movie = movieMapper.toEntity(request);
        return movieMapper.toResponse(movieRepository.save(movie));
    }

    @Override
    public MovieResponse update(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + id));
        movieMapper.updateEntityFromRequest(request, movie);
        return movieMapper.toResponse(movieRepository.save(movie));
    }

    @Override
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    public MovieResponse getById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + id));
        return movieMapper.toResponse(movie);
    }

    @Override
    public Page<MovieResponse> search(String genre, Pageable pageable) {
        Page<Movie> movies = (genre == null || genre.isBlank())
                ? movieRepository.findByActiveTrue(pageable)
                : movieRepository.findByActiveTrueAndGenreContainingIgnoreCase(genre, pageable);
        return movies.map(movieMapper::toResponse);
    }
}
