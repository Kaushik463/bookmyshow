package in.strikes.bookmyshow.mapper;

import in.strikes.bookmyshow.dto.request.MovieRequest;
import in.strikes.bookmyshow.dto.response.MovieResponse;
import in.strikes.bookmyshow.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toEntity(MovieRequest request);
    MovieResponse toResponse(Movie movie);
    void updateEntityFromRequest(MovieRequest request, @MappingTarget Movie movie);
}
