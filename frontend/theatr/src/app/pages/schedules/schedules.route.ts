import { Routes } from "@angular/router";
import { MovieScheduleComponent } from "./movie-schedule/movie-schedule.component";
import { BookScheduleComponent } from "./book-schedule/book-schedule.component";
import { accessGuard } from "../../main/guards/access.guard";

export const scheduleRoutes: Routes = [
    {
        path: ':id',
        data: { roles: ['ROLE_CUSTOMER', 'ROLE_GUEST'] },
        canActivate: [accessGuard],
        component: MovieScheduleComponent
    },
    {
        path: ':id/book',
        data: { roles: ['ROLE_CUSTOMER'] },
        canActivate: [accessGuard],
        component: BookScheduleComponent
    }
]