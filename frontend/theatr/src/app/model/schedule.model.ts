import { Booking } from "./booking.model";
import { Movie } from "./movie.model";
import { Studio } from "./studio.model";

export interface Schedule {
    id?: string;
    movieId?: string;
    studioId?: number;
    price?: number;
    showDate?: Date;
    movie?: Movie;
    studio?: Studio;
    booking?: Booking;
    movieIds?: Movie[];
    studioIds?: Studio[];
}

export interface HomeSchedule {
    id?: string;
    movieId?: string;
    movieTitle?: string;
    posterUrl?: string;
    movieSynopsis?: string;
    movieYear?: number;
    movieDuration?: number;
    studioName?: string;
}

export interface AddScheduleDTO {
    showDate: Date;
    movieId: String[];
    studioId: number[];
    price: number;
}

export interface CreateScheduleDTO {
    showDate: string;
    movieId: String[];
    studioId: number[];
    price: number;
}

export interface DetailSchedule {
    movieId?: string;
    movieTitle?: string;
    movieYear?: number;
    movieDuration?: number;
    movieSynopsis?: string;
    posterUrl?: string;
    shows?: Show[];
}

export interface Show {
    scheduleId?: string;
    studioId?: number;
    showDate?: Date;
    studioName?: string;
    price?: number;
}