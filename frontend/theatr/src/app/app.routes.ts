import { Routes } from '@angular/router';
import { RouterConfig } from './config/app.constants';

export const routes: Routes = [
    {
        path: RouterConfig.LOGIN.path,
        loadChildren: () =>
            import('./pages/login/login.routes').then((m) => m.loginRoutes),
        title: RouterConfig.LOGIN.title,
        data: RouterConfig.LOGIN.data,
    },
    {
        path: RouterConfig.HOME.path,
        loadChildren: () =>
            import('./pages/home/home.routes').then((m) => m.homeRoutes),
        title: RouterConfig.HOME.title,
    }
];
