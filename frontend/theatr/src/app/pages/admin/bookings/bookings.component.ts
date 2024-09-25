import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Booking, BookingHistoryResponse } from '../../../model/booking.model';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { FullTimePipe } from '../../../core/pipes/full-time/full-time.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { BookingService } from '../../../services/booking/booking.service';
import { NormalFullDateTimePipe } from '../../../core/pipes/normal-full-date-time/normal-full-date-time.pipe';
import { NormalTimeFormatPipe } from '../../../core/pipes/normal-time-format/normal-time-format.pipe';

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

  currentPage = 1;
  totalPages = 1;
  sortDir = "DESC";

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

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
    this.bookingService.getAdminHistory(page, 10, this.sortDir).subscribe({
      next: (res: any) => {
        this.bookingList = res.content;
        this.currentPage = res.pageable.pageNumber + 1;
        this.totalPages = res.totalPages;
      },
      error: (err) => {
        this.showAlert('Failed to get booking list: ' + err.error.message, false);
      }
    });
  }

  showModal(booking: Booking) {
    this.currentBooking = booking;

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
    console.log("Tanggal dipilih: ", this.currentDateTime);
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
