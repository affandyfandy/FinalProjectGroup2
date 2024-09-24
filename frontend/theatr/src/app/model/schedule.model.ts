import { Booking } from "./booking.model";
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
    booking?: Booking;
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