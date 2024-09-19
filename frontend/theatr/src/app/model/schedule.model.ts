import { Movie } from "./movie.model";
import { Studio } from "./studio.model";

export interface Schedule {
    id?: string;
    movieId?: number;
    studioId?: number;
    price?: number;
    showTime?: Date;
    movie?: Movie;
    studio?: Studio;
}

export interface AddScheduleDTO {
    showTime: Date;
    movieId: String;
    studioId: number;
}