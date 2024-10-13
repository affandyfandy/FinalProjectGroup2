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
        path: 'admin/bookings',
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
        path: 'admin/schedules',
        link: '/admin/schedules',
        title: 'Schedules',
        data: { header: true },
    },
    MOVIES: {
        path: 'admin/movies',
        link: '/admin/movies',
        title: 'Movies',
        data: { header: true },
    },
    STUDIOS: {
        path: 'admin/studios',
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

export const MessageConstants = {
    REGISTER_SUCCESS: 'Register success',
    REGIStER_FAILED(message: any): string {
        return `Register failed: ${message.error.message}`;
    },
    LOGIN_SUCCESS: 'Login success',
    LOGIN_FAILED: 'Login failed: Invalid username or password',

    CREATE_BOOKING_SUCCESS: 'Booking created successfully',
    CREATE_BOOKING_FAILED(message: any): string {
        return `Failed to create booking: ${message.error.message}`;
    },
    GET_BOOKING_DETAIL_FAILED(message: any): string {
        return `Failed to get booking detail: ${message.error.message}`;
    },
    GET_BOOKING_LIST_FAILED(message: any): string {
        return `Failed to get booking list detail: ${message.error.message}`;
    },

    GET_MOVIE_LIST_FAILED(message: any): string {
        return `Failed to get movie list: ${message.error.message}`;
    },
    GET_MOVIE_FAILED(message: any): string {
        return `Failed to get movie: ${message.error.message}`;
    },
    UPDATE_MOVIE_SUCCESS: 'Movie updated successfully',
    UPDATE_MOVIE_FAILED(message: any): string {
        return `Failed to update movie: ${message.error.message}`;
    },
    CREATE_MOVIE_SUCCESS: 'Movie created successfully',
    CREATE_MOVIE_FAILED(message: any): string {
        return `Failed to create movie: ${message.error.message}`;
    },
    UPLOAD_POSTER_FAILED(message: any): string {
        return `Failed to upload poster: ${message.error.message}`;
    },

    GET_STUDIO_LIST_FAILED(message: any): string {
        return `Failed to get studio list: ${message.error.message}`;
    },
    ADD_STUDIO_SUCCESS: 'Studio added successfully',
    ADD_STUDIO_FAILED(message: any): string {
        return `Failed to add studio: ${message.error.message}`;
    },
    EDIT_STUDIO_SUCCESS: 'Studio edited successfully',
    EDIT_STUDIO_FAILED(message: any): string {
        return `Failed to edit studio: ${message.error.message}`;
    },
    UPDATE_STUDIO_STATUS_SUCCESS: 'Studio status updated successfully',
    UPDATE_STUDIO_STATUS_FAILED(message: any): string {
        return `Failed to update studio status: ${message.error.message}`;
    },

    PRINT_TICKET_SUCCESS: 'Ticket printed successfully',
    PRINT_TICKET_FAILED(message: any): string {
        return `Failed to print ticket: ${message.error.message}`;
    },

    GET_SCHEDULE_LIST_FAILED(message: any): string {
        return `Failed to get schedule list: ${message.error.message}`;
    },
    GET_SCHEDULE_FAILED(message: any): string {
        return `Failed to get schedule data: ${message.error.message}`;
    },
    CREATE_SCHEDULE_SUCCESS: 'Schedule created successfully',
    CREATE_SCHEDULE_FAILED(message: any): string {
        return `Failed to create schedule: ${message.error.message}`;
    },

    GET_PROFILE_FAILED(message: any): string {
        return `Failed to get profile: ${message.error.message}`;
    },
    UPDATE_PROFILE_SUCCESS: 'Profile updated successfully',
    UPDATE_PROFILE_FAILED(message: any): string {
        return `Failed to update profile: ${message.error.message}`;
    },
    TOP_UP_SUCCESS: 'Top up success',
    TOP_UP_FAILED(message: any): string {
        return `Top up failed: ${message.error.message}`;
    },
    CHANGE_PASSWORD_SUCCESS: 'Password changed successfully',
    CHANGE_PASSWORD_FAILED(message: any): string {
        return `Failed to change password: ${message.error.message}`;
    },
}