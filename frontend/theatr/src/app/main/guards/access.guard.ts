import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

export const accessGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const requiredRoles = route.data['roles'];

    if (authService.isLoggedIn()) {
        const userRole = authService.getRole();

        if (requiredRoles.includes(userRole)) {
            return true;
        } else {
            router.navigate(['/unauthorized']);
            return false;
        }
    }

    router.navigate(['/login']);
    return false;
};