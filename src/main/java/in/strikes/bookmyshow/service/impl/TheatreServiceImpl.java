package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.dto.request.TheatreRequest;
import in.strikes.bookmyshow.dto.response.TheatreResponse;
import in.strikes.bookmyshow.entity.Theatre;
import in.strikes.bookmyshow.exception.ResourceNotFoundException;
import in.strikes.bookmyshow.mapper.TheatreMapper;
import in.strikes.bookmyshow.repository.TheatreRepository;
import in.strikes.bookmyshow.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final TheatreMapper theatreMapper;

    @Override
    public TheatreResponse create(TheatreRequest request) {
        Theatre theatre = theatreMapper.toEntity(request);
        return theatreMapper.toResponse(theatreRepository.save(theatre));
    }

    @Override
    public TheatreResponse update(Long id, TheatreRequest request) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found: " + id));
        theatreMapper.updateEntityFromRequest(request, theatre);
        return theatreMapper.toResponse(theatreRepository.save(theatre));
    }

    @Override
    public void delete(Long id) {
        if (!theatreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theatre not found: " + id);
        }
        theatreRepository.deleteById(id);
    }

    @Override
    public TheatreResponse getById(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found: " + id));
        return theatreMapper.toResponse(theatre);
    }

    @Override
    public List<TheatreResponse> getByCity(String city) {
        return theatreRepository.findByCityIgnoreCase(city)
                .stream().map(theatreMapper::toResponse).toList();
    }
}
