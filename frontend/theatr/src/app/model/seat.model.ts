export interface Seat {
    id: number;
    seatCode: string;
    status?: 'AVAILABLE' | 'BOOKED';
}