package in.strikes.bookmyshow.service;

import in.strikes.bookmyshow.dto.request.ShowRequest;
import in.strikes.bookmyshow.dto.response.ShowResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowService {

    ShowResponse create(ShowRequest request);
    List<ShowResponse> getByMovieAndDateRange(Long movieId, LocalDateTime start, LocalDateTime end);
}
