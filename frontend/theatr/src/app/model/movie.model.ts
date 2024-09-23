export interface Movie {
    id?: string;
    title?: string;
    synopsis?: string;
    duration?: number;
    posterUrl?: string;
    year?: number;
}

export interface SaveMovieDTO {
    title: string;
    synopsis: string;
    posterUrl: string;
    year: number;
}

export interface ShowMovieDTO {
    title: string;
    synopsis: string;
    duration: number,
    posterUrl: string;
    year: number;
}