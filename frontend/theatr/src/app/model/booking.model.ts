import { DetailSchedule, Schedule } from "./schedule.model";
import { Seat } from "./seat.model";
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