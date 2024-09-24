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

  private getAuthHeaders() {
    return {
      'Authorization': `Bearer ${localStorage.getItem(AppConstants.TOKEN_KEY)}`,
    };
  }

  getAllMovies(page: number = 0, size: number = 10) {
    const headers = this.getHeaders();
    const params = {
      page: page.toString(),
      size: size.toString()
    };

    return this.http.get(`${this.apiUrl}/admin/get-all`, { headers, params });
  }

  getMovieById(id: string) {
    const headers = this.getHeaders();

    return this.http.get(`${this.apiUrl}/anonymous/${id}`, { headers });
  }

  uploadImagePoster(file: File) {
    const headers = this.getAuthHeaders();
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.apiUrl}/admin/upload-poster`, formData, { headers: headers });
  }

  createMovie(body: SaveMovieDTO) {
    const headers = this.getHeaders();

    return this.http.post(`${this.apiUrl}/admin/create-movie`, body, { headers });
  }

  updateMovie(id: string, body: SaveMovieDTO) {
    const headers = this.getHeaders();

    return this.http.put(`${this.apiUrl}/admin/${id}`, body, { headers });
  }
}
