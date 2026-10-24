package in.strikes.bookmyshow.service;

import in.strikes.bookmyshow.dto.request.ScreenRequest;
import in.strikes.bookmyshow.dto.response.ScreenResponse;

public interface ScreenService {
    ScreenResponse create(ScreenRequest request);
    ScreenResponse getById(Long id);
    void delete(Long id);
}
