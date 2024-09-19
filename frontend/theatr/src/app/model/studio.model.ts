import { Seat } from "./seat.model";

export interface Studio {
    id?: string;
    name?: string;
    deleted?: boolean;
    seats?: Seat[];
}