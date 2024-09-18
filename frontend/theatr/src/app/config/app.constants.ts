export const AppConstants = {
    BASE_API_URL: 'http://localhost:8080/api/v1'
}

export const RouterConfig = {
    HOME: {
        path: '',
        link: '/',
        title: 'Home'
    },
    LOGIN: {
        path: 'login',
        link: '/login',
        title: 'Login',
        data: { header: true },
    },
    BOOKINGS: {
        path: 'bookings',
        link: '/bookings',
        title: 'Bookings',
        data: { header: true },
    },
    SCHEDULES: {
        path: 'schedules',
        link: '/schedules',
        title: 'Schedules',
        data: { header: true },
    },
    MOVIES: {
        path: 'movies',
        link: '/movies',
        title: 'Movies',
        data: { header: true },
    },
    STUDIOS: {
        path: 'studios',
        link: '/studios',
        title: 'Studios',
        data: { header: true },
    },
    PROFILE: {
        path: 'profile',
        link: '/profile',
        title: 'Profile',
        data: { header: true },
    },
}