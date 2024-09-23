export interface Seat {
    id: number;
    seatCode: string;
    status?: 'ACTIVE' | 'INACTIVE';
}