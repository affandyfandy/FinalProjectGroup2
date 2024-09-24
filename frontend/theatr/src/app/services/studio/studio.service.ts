import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AppConstants } from '../../config/app.constants';
import { AddStudioDTO } from '../../model/studio.model';

@Injectable({
  providedIn: 'root'
})
export class StudioService {

  private apiUrl: string = `${AppConstants.BASE_API_URL}/studios`;

  constructor(private http: HttpClient) { }

  private getHeaders() {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem(AppConstants.TOKEN_KEY)}`
    };
  }

  private getAuthHeaders() {
    return {
      'Authorization': `Bearer ${localStorage.getItem(AppConstants.TOKEN_KEY)}`
    };
  }

  getStudioList(page: number = 0, size: number = 10) {
    return this.http.get(`${this.apiUrl}`, {
      headers: this.getHeaders(),
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  getActiveStudioList() {
    return this.http.get(`${this.apiUrl}/active`, {
      headers: this.getHeaders(),
    });
  }

  addStudio(body: AddStudioDTO) {
    return this.http.post(`${this.apiUrl}/admin/create-studio`, body, { headers: this.getHeaders() });
  }

  editStudio(id: any, body: AddStudioDTO) {
    return this.http.put(`${this.apiUrl}/admin/${id}`, body, { headers: this.getHeaders() });
  }

  changeStatus(id: number) {
    return this.http.patch(`${this.apiUrl}/admin/${id}/change-status`, null, { headers: this.getHeaders() });
  }
}
