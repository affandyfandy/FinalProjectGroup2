import { Routes } from "@angular/router";
import { HistoryComponent } from "./history/history.component";
import { HistoryDetailComponent } from "./history-detail/history-detail.component";

export const bookingRoutes: Routes = [
    { path: '', component: HistoryComponent },
    { path: ':id', component: HistoryDetailComponent },
]