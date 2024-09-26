import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Booking, BookingDetailResponse, BookingHistoryResponse } from '../../../model/booking.model';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { FullTimePipe } from '../../../core/pipes/full-time/full-time.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { BookingService } from '../../../services/booking/booking.service';
import { NormalFullDateTimePipe } from '../../../core/pipes/normal-full-date-time/normal-full-date-time.pipe';
import { NormalTimeFormatPipe } from '../../../core/pipes/normal-time-format/normal-time-format.pipe';
import { MessageConstants } from '../../../config/app.constants';

@Component({
  selector: 'app-bookings',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FullDateTimePipe,
    FullTimePipe,
    PriceFormatPipe,
    NormalFullDateTimePipe,
    NormalTimeFormatPipe
  ],
  providers: [
    BookingService
  ],
  templateUrl: './bookings.component.html'
})
export class BookingsComponent implements OnInit {

  currentDateTime = '';

  bookingList: BookingHistoryResponse[] = [];
  currentBooking: BookingHistoryResponse = {};
  bookingData: BookingDetailResponse = {};

  currentPage = 1;
  totalPages = 1;
  sortDir = "DESC";

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  isDetailLoading = false;

  constructor(
    private bookingService: BookingService
  ) { }

  ngOnInit(): void {
    this.currentDateTime = this.getTodayDate();
    this.getBookingList();
  }

  getTodayDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  getBookingList(page: number = 0) {
    this.bookingService.getAdminHistory(page, 10, this.sortDir, this.currentDateTime).subscribe({
      next: (res: any) => {
        this.bookingList = res.content;
        this.currentPage = res.pageable.pageNumber + 1;
        this.totalPages = res.totalPages;
      },
      error: (err) => {
        this.showAlert(MessageConstants.GET_BOOKING_LIST_FAILED(err), false);
      }
    });
  }

  getBookingData(bookingId: string) {
    if (!!bookingId) {
      this.isDetailLoading = true;
      this.bookingService.getDetailBooking(bookingId).subscribe({
        next: (res: any) => {
          this.bookingData = res;
          this.isDetailLoading = false;
        },
        error: (err: any) => {
          this.isDetailLoading = false;
          this.showAlert(MessageConstants.GET_BOOKING_DETAIL_FAILED(err), false);
        },
        complete: () => {
          this.isDetailLoading = false;
        }
      });
    }
  }

  showModal(id: string) {
    this.getBookingData(id);

    const modal = document.getElementById('booking-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeModal() {
    this.currentBooking = {};
    const modal = document.getElementById('booking-modal') as HTMLDialogElement;
    modal.close();
  }

  onDateChange(event: any) {
    this.currentDateTime = event.target.value;
    this.getBookingList();
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
    this.getBookingList();
  }
}
