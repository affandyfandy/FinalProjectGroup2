import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AppConstants } from '../../config/app.constants';
import { AddScheduleDTO, CreateScheduleDTO } from '../../model/schedule.model';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {

  private apiUrl: string = `${AppConstants.BASE_API_URL}/schedules`

  constructor(private http: HttpClient) { }

  private getHeaders() {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem(AppConstants.TOKEN_KEY)}`
    };
  }

  private getContenttHeaders() {
    return {
      'Content-Type': 'application/json'
    };
  }

  getAllSchedules(date: string, page: number = 0, size: number = 10) {
    return this.http.get(`${this.apiUrl}/admin`, {
      headers: this.getHeaders(),
      params: {
        date: date.toString(),
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  getAvailableSchedule(page: number = 0, size: number = 6, showDate: string) {
    const headers = this.getHeaders();
    const params: any = {
      showDate: showDate.toString(),
      page: page.toString(),
      size: size.toString()
    };

    return this.http.get(`${this.apiUrl}/available`, { headers, params });
  }

  createSchedule(body: CreateScheduleDTO) {
    return this.http.post(`${this.apiUrl}/admin/create-schedule`, body, { headers: this.getHeaders() });
  }

  getScheduleByMovieId(movieId: string, page: number = 0, size: number = 10, showDate: string) {
    const headers = this.getHeaders();
    const params = {
      page: page.toString(),
      size: size.toString(),
      showDate: showDate.toString()
    };

    return this.http.get(`${this.apiUrl}/movie/${movieId}`, { headers, params });
  }
}
