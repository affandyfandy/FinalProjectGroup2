export interface Seat {
    id: number;
    seat_code: string;
    status: 'ACTIVE' | 'INACTIVE';
}