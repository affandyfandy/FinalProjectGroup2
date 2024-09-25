import { Movie } from "./movie.model";
import { DetailSchedule, Schedule } from "./schedule.model";
import { Seat } from "./seat.model";
import { Studio } from "./studio.model";
import { User } from "./user.model";

export interface Booking {
    id?: string;
    totalAmount?: number;
    isPrinted?: boolean;
    user?: User;
    schedule?: Schedule;
    seats?: Seat[];
    updatedTime?: Date;
}

export interface CreateBookingDTO {
    scheduleIds: string[];
    seatIds: number[];
    totalAmount: number;
}

export interface BookingHistoryResponse {
    id?: string;
    totalAmount?: number;
    isPrinted?: boolean;
    updatedTime?: Date;
    scheduleIds?: DetailSchedule[];
}

export interface BookingDetailResponse {
    bookingId?: string;
    createdAt?: Date;
    price?: number;
    totalAmount?: number;
    custId?: string;
    custName?: string;
    showDate?: Date;
    isPrinted?: boolean;
    movies?: Movie[];
    studios?: Studio[];
    seats?: Seat[];
}