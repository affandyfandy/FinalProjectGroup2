import { Injectable } from '@angular/core';
import { AppConstants } from '../../config/app.constants';
import { HttpClient } from '@angular/common/http';
import { ChangePasswordDTO, TopUpDTO, UpdateProfileDTO } from '../../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl: string = `${AppConstants.BASE_API_URL}/users`;

  constructor(private http: HttpClient) { }

  private getHeaders() {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem(AppConstants.TOKEN_KEY)}`
    };
  }

  getProfile() {
    return this.http.get(`${this.apiUrl}/profile`, { headers: this.getHeaders() });
  }

  topUpBalance(body: TopUpDTO) {
    return this.http.put(`${this.apiUrl}/top-up`, body, { headers: this.getHeaders() });
  }

  updateProfile(body: UpdateProfileDTO) {
    return this.http.put(`${this.apiUrl}/update-profile`, body, { headers: this.getHeaders() });
  }

  changePassword(body: ChangePasswordDTO) {
    return this.http.put(`${this.apiUrl}/change-password`, body, { headers: this.getHeaders() });
  }
}
