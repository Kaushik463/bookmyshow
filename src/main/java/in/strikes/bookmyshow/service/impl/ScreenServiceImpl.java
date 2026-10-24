package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.dto.request.ScreenRequest;
import in.strikes.bookmyshow.dto.response.ScreenResponse;
import in.strikes.bookmyshow.entity.Screen;
import in.strikes.bookmyshow.entity.Theatre;
import in.strikes.bookmyshow.exception.ResourceNotFoundException;
import in.strikes.bookmyshow.mapper.ScreenMapper;
import in.strikes.bookmyshow.repository.ScreenRepository;
import in.strikes.bookmyshow.repository.TheatreRepository;
import in.strikes.bookmyshow.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenMapper screenMapper;

    @Override
    public ScreenResponse create(ScreenRequest request) {
        Theatre theatre = theatreRepository.findById(request.theatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found: " + request.theatreId()));

        Screen screen = Screen.builder()
                .name(request.name())
                .totalSeats(request.totalSeats())
                .theatre(theatre)
                .build();

        return screenMapper.toResponse(screenRepository.save(screen));
    }

    @Override
    public ScreenResponse getById(Long id) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found: " + id));
        return screenMapper.toResponse(screen);
    }

    @Override
    public void delete(Long id) {
        if (!screenRepository.existsById(id)) {
            throw new ResourceNotFoundException("Screen not found: " + id);
        }
        screenRepository.deleteById(id);
    }
}
