package in.strikes.bookmyshow.mapper;

import in.strikes.bookmyshow.dto.response.ScreenResponse;
import in.strikes.bookmyshow.entity.Screen;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScreenMapper {

    default ScreenResponse toResponse(Screen screen) {
        return new ScreenResponse(
                screen.getId(),
                screen.getName(),
                screen.getTotalSeats(),
                screen.getTheatre().getId()
        );
    }
}
