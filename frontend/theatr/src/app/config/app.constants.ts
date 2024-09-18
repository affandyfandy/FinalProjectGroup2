export const AppConstants = {
    BASE_API_URL: 'http://localhost:8080/api/v1',
    TOKEN_KEY: 'TOKEN',
    ROLE_KEY: 'ROLE',
}

export const RouterConfig = {
    HOME: {
        path: '',
        link: '/',
        title: 'Home',
        data: { header: true },
    },
    LOGIN: {
        path: 'login',
        link: '/login',
        title: 'Login',
    },
    BOOKINGS: {
        path: 'bookings',
        link: '/bookings',
        title: 'Bookings',
        data: { header: true },
    },
    ADMIN_BOOKINGS: {
        path: 'admin-bookings',
        link: '/admin/bookings',
        title: 'Bookings',
        data: { header: true },
    },
    SCHEDULES: {
        path: 'schedules',
        link: '/schedules',
        title: 'Schedules',
        data: { header: true },
    },
    ADMIN_SCHEDULES: {
        path: 'admin-schedules',
        link: '/admin/schedules',
        title: 'Schedules',
        data: { header: true },
    },
    MOVIES: {
        path: 'movies',
        link: '/admin/movies',
        title: 'Movies',
        data: { header: true },
    },
    STUDIOS: {
        path: 'studios',
        link: '/admin/studios',
        title: 'Studios',
        data: { header: true },
    },
    PROFILE: {
        path: 'profile',
        link: '/profile',
        title: 'Profile',
        data: { header: true },
    },
    UNAUTHORIZED: {
        path: 'unauthorized',
        link: '/unauthorized',
        title: 'Unauthorized',
    },
    NOT_FOUND: {
        path: 'not-found',
        link: '/not-found',
        title: 'Not Found',
    },
}