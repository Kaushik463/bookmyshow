package in.strikes.bookmyshow.mapper;

import in.strikes.bookmyshow.dto.request.TheatreRequest;
import in.strikes.bookmyshow.dto.response.TheatreResponse;
import in.strikes.bookmyshow.entity.Theatre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TheatreMapper {

    Theatre toEntity(TheatreRequest request);
    TheatreResponse toResponse(Theatre theatre);
    void updateEntityFromRequest(TheatreRequest request, @MappingTarget Theatre theatre);
}
