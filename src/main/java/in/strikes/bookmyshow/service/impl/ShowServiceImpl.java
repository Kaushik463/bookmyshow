package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.dto.request.ShowRequest;
import in.strikes.bookmyshow.dto.response.ShowResponse;
import in.strikes.bookmyshow.entity.*;
import in.strikes.bookmyshow.enums.SeatStatus;
import in.strikes.bookmyshow.exception.ResourceNotFoundException;
import in.strikes.bookmyshow.repository.*;
import in.strikes.bookmyshow.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final ShowSeatRepository showSeatRepository;

    @Override
    public ShowResponse create(ShowRequest request) {
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + request.movieId()));
        Screen screen = screenRepository.findById(request.screenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found: " + request.screenId()));

        Show show = Show.builder()
                .startTime(request.startTime())
                .endTime(request.endTime())
                .basePrice(request.basePrice())
                .movie(movie)
                .screen(screen)
                .build();

        show = showRepository.save(show);

        // Auto-generate ShowSeat rows for every seat on this screen
        List<Seat> seats = seatRepository.findByScreenId(screen.getId());
        Show finalShow = show;
        List<ShowSeat> showSeats = seats.stream()
                .map(seat -> ShowSeat.builder()
                        .show(finalShow)
                        .seat(seat)
                        .status(SeatStatus.AVAILABLE)
                        .price(request.basePrice())
                        .build())
                .toList();
        showSeatRepository.saveAll(showSeats);

        return toResponse(show);
    }

    @Override
    public List<ShowResponse> getByMovieAndDateRange(Long movieId, LocalDateTime start, LocalDateTime end) {
        return showRepository.findByMovieIdAndStartTimeBetween(movieId, start, end)
                .stream().map(this::toResponse).toList();
    }

    private ShowResponse toResponse(Show show) {
        return new ShowResponse(
                show.getId(),
                show.getStartTime(),
                show.getEndTime(),
                show.getBasePrice(),
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getScreen().getId()
        );
    }
}
