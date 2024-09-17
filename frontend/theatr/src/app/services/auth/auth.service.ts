import { Injectable } from '@angular/core';
import { AppConstants } from '../../config/app.constants';
import { HttpClient } from '@angular/common/http';
import { LoginDTO, RegisterDTO } from '../../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl: string = `${AppConstants.BASE_API_URL}/auth`;

  constructor(private http: HttpClient) { }

  private getHeaders() {
    return {
      'Content-Type': 'application/json',
    };
  }

  login(body: LoginDTO) {
    return this.http.post(`${this.apiUrl}/login`, body, { headers: this.getHeaders() });
  }

  register(body: RegisterDTO) {
    return this.http.post(`${this.apiUrl}/register`, body, { headers: this.getHeaders() });
  }

  setToken(token: string) {
    localStorage.setItem(AppConstants.TOKEN_KEY, token);
  }

  getToken() {
    return localStorage.getItem(AppConstants.TOKEN_KEY);
  }

  setRole(role: string) {
    localStorage.setItem(AppConstants.ROLE_KEY, role);
  }

  getRole() {
    return localStorage.getItem(AppConstants.ROLE_KEY);
  }

  logout() {
    localStorage.removeItem(AppConstants.TOKEN_KEY);
    localStorage.removeItem(AppConstants.ROLE_KEY);
  }

  isLoggedIn() {
    return !!this.getToken();
  }
}
