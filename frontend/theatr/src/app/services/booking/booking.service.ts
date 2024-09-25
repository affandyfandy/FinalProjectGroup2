import { Injectable } from '@angular/core';
import { AppConstants } from '../../config/app.constants';
import { HttpClient } from '@angular/common/http';
import { CreateBookingDTO } from '../../model/booking.model';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private apiUrl: string = `${AppConstants.BASE_API_URL}/bookings`

  constructor(private http: HttpClient) { }

  private getHeaders() {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem(AppConstants.TOKEN_KEY)}`
    };
  }

  getAdminHistory(page: number = 0, size: number = 10, sortDir: string) {
    return this.http.get(`${this.apiUrl}/admin/history`, {
      headers: this.getHeaders(),
      params: {
        page: page.toString(),
        size: size.toString(),
        sortDir: sortDir
      }
    });
  }

  createBooking(body: CreateBookingDTO) {
    return this.http.post(`${this.apiUrl}/customer/book`, body, {
      headers: this.getHeaders()
    });
  }
}
