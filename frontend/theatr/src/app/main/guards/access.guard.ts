import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { RouterConfig } from '../../config/app.constants';

export const accessGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const requiredRoles = route.data['roles'];

    if (authService.isLoggedIn()) {
        const userRole = authService.getRole();
        console.log('userRole', userRole);
        console.log('requiredRoles', requiredRoles);

        if (requiredRoles.includes(userRole)) {
            return true;
        } else {
            if (authService.isAdmin()) {
                router.navigate([RouterConfig.ADMIN_SCHEDULES.link]);
                return false;
            } else {
                router.navigate([RouterConfig.UNAUTHORIZED.link]);
                return false;
            }
        }
    }

    router.navigate([RouterConfig.LOGIN.link]);
    return false;
};