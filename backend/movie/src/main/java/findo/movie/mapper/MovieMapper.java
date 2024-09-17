package findo.movie.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import findo.movie.data.entity.Movie;
import findo.movie.dto.MovieDTO;
import findo.movie.dto.MovieSaveDTO;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);
 
    // Movie - MovieDTO
    MovieDTO toMovieDTO(Movie movie);

    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Movie toMovie(MovieDTO movieDTO);

    // Movie - MovieSaveDTO
    MovieSaveDTO toMovieSaveDTO(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Movie toMovie(MovieSaveDTO movieSaveDTO);
}
