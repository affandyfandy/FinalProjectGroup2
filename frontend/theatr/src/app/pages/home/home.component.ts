import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HomeSchedule, Schedule } from '../../model/schedule.model';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterConfig } from '../../config/app.constants';
import { ScheduleService } from '../../services/schedule/schedule.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  providers: [
    ScheduleService
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  currentDateTime = '';

  slidingBanner: HomeSchedule[] = []
  scheduleList: HomeSchedule[] = []

  currentPage = 1;
  totalPages = 1;

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  isLoading = false;

  constructor(
    private router: Router,
    private scheduleService: ScheduleService
  ) { }

  ngOnInit(): void {
    this.currentDateTime = this.getTodayDate();
    this.getSlidingBanner();
    this.getScheduleList();
  }

  getTodayDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  onDateChange(event: any) {
    this.currentDateTime = event.target.value;
    this.getScheduleList();
  }

  getSlidingBanner() {
    this.isLoading = true;
    this.scheduleService.getAvailableSchedule(0, 6, this.currentDateTime).subscribe({
      next: (res: any) => {
        this.slidingBanner = res.content;
        this.isLoading = false;
      },
      error: (err) => {
        this.showAlert('Failed to get schedule list banner: ' + err.error.message, false);
        this.isLoading = false;
      }
    });
  }

  getScheduleList(page: number = 0) {
    this.isLoading = true;
    this.scheduleService.getAvailableSchedule(page, 10, this.currentDateTime).subscribe({
      next: (res: any) => {
        this.scheduleList = res?.content ?? [];
        this.currentPage = page;
        this.totalPages = res.page.totalPages;
        this.isLoading = false;
      },
      error: (err) => {
        this.showAlert('Failed to get schedule list: ' + err.error.message, false);
        this.isLoading = false;
      }
    });
  }

  navigateToSchedule(id: string) {
    this.router.navigate([RouterConfig.SCHEDULES.path, id]);
  }

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }
}
