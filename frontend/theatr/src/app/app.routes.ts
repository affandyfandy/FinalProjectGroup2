import { Routes } from '@angular/router';
import { RouterConfig } from './config/app.constants';
import { accessGuard } from './main/guards/access.guard';
import { UnauthorizedComponent } from './pages/general/unauthorized/unauthorized.component';
import { NotFoundComponent } from './pages/general/not-found/not-found.component';

export const routes: Routes = [
    {
        path: RouterConfig.LOGIN.path,
        loadChildren: () =>
            import('./pages/login/login.routes').then((m) => m.loginRoutes),
        title: RouterConfig.LOGIN.title,
    },
    {
        path: RouterConfig.HOME.path,
        data: { roles: ['ROLE_GUEST', 'ROLE_CUSTOMER'] },
        canActivate: [accessGuard],
        loadChildren: () =>
            import('./pages/home/home.routes').then((m) => m.homeRoutes),
        title: RouterConfig.HOME.title,
    },
    {
        path: RouterConfig.PROFILE.path,
        data: { roles: ['ROLE_ADMIN', 'ROLE_CUSTOMER'] },
        canActivate: [accessGuard],
        loadChildren: () =>
            import('./pages/profile/profile.routes').then((m) => m.profileRoutes),
        title: RouterConfig.PROFILE.title,
    },
    {
        path: RouterConfig.BOOKINGS.path,
        data: { roles: ['ROLE_CUSTOMER'] },
        canActivate: [accessGuard],
        loadChildren: () =>
            import('./pages/bookings/booking.routes').then((m) => m.bookingRoutes),
        title: RouterConfig.BOOKINGS.title,
    },
    {
        path: RouterConfig.SCHEDULES.path,
        loadChildren: () =>
            import('./pages/schedules/schedules.route').then((m) => m.scheduleRoutes),
        title: RouterConfig.SCHEDULES.title,
    },
    {
        path: RouterConfig.ADMIN_BOOKINGS.path,
        data: { roles: ['ROLE_ADMIN'] },
        canActivate: [accessGuard],
        loadChildren: () =>
            import('./pages/admin/bookings/admin-bookings.routes').then((m) => m.adminBookingRoutes),
        title: RouterConfig.ADMIN_BOOKINGS.title,
    },
    {
        path: RouterConfig.MOVIES.path,
        data: { roles: ['ROLE_ADMIN'] },
        canActivate: [accessGuard],
        loadChildren: () =>
            import('./pages/admin/movie/admin-movie.routes').then((m) => m.adminMovieRoutes),
        title: RouterConfig.MOVIES.title,
    },
    {
        path: RouterConfig.ADMIN_SCHEDULES.path,
        data: { roles: ['ROLE_ADMIN'] },
        canActivate: [accessGuard],
        loadChildren: () =>
            import('./pages/admin/schedule/admin-schedule.routes').then((m) => m.adminScheduleRoutes),
        title: RouterConfig.ADMIN_SCHEDULES.title,
    },
    {
        path: RouterConfig.STUDIOS.path,
        data: { roles: ['ROLE_ADMIN'] },
        canActivate: [accessGuard],
        loadChildren: () =>
            import('./pages/admin/studio/admin-studio.routes').then((m) => m.adminStudioRoutes),
        title: RouterConfig.STUDIOS.title,
    },
    {
        path: RouterConfig.UNAUTHORIZED.path,
        component: UnauthorizedComponent
    },
    {
        path: '**',
        component: NotFoundComponent
    }
];
