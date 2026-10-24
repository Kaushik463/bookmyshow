package in.strikes.bookmyshow.service;

import in.strikes.bookmyshow.dto.request.TheatreRequest;
import in.strikes.bookmyshow.dto.response.TheatreResponse;

import java.util.List;

public interface TheatreService {

    TheatreResponse create(TheatreRequest request);
    TheatreResponse update(Long id, TheatreRequest request);
    void delete(Long id);
    TheatreResponse getById(Long id);
    List<TheatreResponse> getByCity(String city);
}
