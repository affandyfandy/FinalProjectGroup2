export interface Movie {
    id?: string;
    title?: string;
    synopsis?: string;
    duration?: number;
    posterUrl?: string;
    year?: number;
}

export interface AddMovieDTO {
    title: string;
    synopsis: string;
    duration: number;
    posterUrl: string;
    year: number;
}