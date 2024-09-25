import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Schedule } from '../../../model/schedule.model';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { Router } from '@angular/router';
import { RouterConfig } from '../../../config/app.constants';
import { BookingService } from '../../../services/booking/booking.service';
import { BookingHistoryResponse } from '../../../model/booking.model';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FullDateTimePipe
  ],
  providers: [
    BookingService
  ],
  templateUrl: './history.component.html'
})
export class HistoryComponent implements OnInit {

  currentDateTime = '';

  scheduleList: BookingHistoryResponse[] = [];

  currentPage = 1;
  totalPages = 1;
  sortDir = "DESC";

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  constructor(
    private router: Router,
    private bookingService: BookingService
  ) { }

  ngOnInit(): void {
    this.currentDateTime = this.getTodayDate();
    this.getScheduleList();
  }

  getTodayDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  getScheduleList(page: number = 0) {
    this.bookingService.getCustomerHistory(page, 10, this.sortDir, this.currentDateTime).subscribe({
      next: (res: any) => {
        this.scheduleList = res.content;
        this.currentPage = res.pageable.pageNumber + 1;
        this.totalPages = res.totalPages;
      },
      error: (err) => {
        this.showAlert('Failed to get booking list: ' + err.error.message, false);
      }
    });
  }

  onDateChange(event: any) {
    this.currentDateTime = event.target.value;
    this.getScheduleList();
  }

  navigateToDetail(id: string) {
    this.router.navigate([RouterConfig.BOOKINGS.path, id]);
  }

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }

  onOrderPickerChange(event: Event) {
    const selectedOrder = (event.target as HTMLSelectElement).value;
    this.sortDir = selectedOrder;
    this.getScheduleList();
  }
}
