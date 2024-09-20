import { Seat } from "./seat.model";

export interface Studio {
    id?: number;
    name?: string;
    deleted?: boolean;
    seats?: Seat[];
}