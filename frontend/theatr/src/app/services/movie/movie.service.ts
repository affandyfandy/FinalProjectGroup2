import { Injectable } from '@angular/core';
import { AppConstants } from '../../config/app.constants';
import { HttpClient } from '@angular/common/http';
import { SaveMovieDTO } from '../../model/movie.model';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  private apiUrl: string = `${AppConstants.BASE_API_URL}/movies`

  constructor(private http: HttpClient) { }

  private getHeaders() {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem(AppConstants.TOKEN_KEY)}`
    };
  }

  getAllMovies(page: number = 0, size: number = 20) {
    const headers = this.getHeaders();
    const params = {
      page: page.toString(),
      size: size.toString()
    };

    return this.http.get(this.apiUrl, { headers, params });
  }

  getMovieById(id: string) {
    const headers = this.getHeaders();

    return this.http.get(`${this.apiUrl}/${id}`, { headers });
  }

  uploadImagePoster() {
    const headers = this.getHeaders();
    
    return this.http.post(`${this.apiUrl}/upload-poster`, { headers });
  }

  createMovie(body: SaveMovieDTO) {
    const headers = this.getHeaders();

    return this.http.post(this.apiUrl, body, { headers });
  }

  updateMovie(id: string, body: SaveMovieDTO) {
    const headers = this.getHeaders();

    return this.http.put(`${this.apiUrl}/${id}`, body, { headers });
  }
}
