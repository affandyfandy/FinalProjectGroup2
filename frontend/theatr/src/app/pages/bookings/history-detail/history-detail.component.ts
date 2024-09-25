import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { BookingDetailResponse } from '../../../model/booking.model';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { BookingService } from '../../../services/booking/booking.service';
import { ActivatedRoute } from '@angular/router';
import { NormalFullDateTimePipe } from '../../../core/pipes/normal-full-date-time/normal-full-date-time.pipe';

@Component({
  selector: 'app-history-detail',
  standalone: true,
  imports: [
    CommonModule,
    PriceFormatPipe,
    FullDateTimePipe,
    NormalFullDateTimePipe
  ],
  providers: [
    BookingService
  ],
  templateUrl: './history-detail.component.html'
})
export class HistoryDetailComponent implements OnInit {

  bookingData: BookingDetailResponse = {};

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private bookingService: BookingService,
  ) { }

  ngOnInit(): void {
    this.getBookingData();
  }

  private getBookingData() {
    const bookingId = this.route.snapshot.paramMap.get('id');
    if (!!bookingId) {
      this.isLoading = true;
      this.bookingService.getDetailBooking(bookingId).subscribe({
        next: (res: any) => {
          this.bookingData = res;
          this.isLoading = false;
        },
        error: (err: any) => {
          this.isLoading = false;
          this.showAlert('Failed to get booking detail: ' + err.error.message, false);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }
  }

  showModal() {
    const modal = document.getElementById('confirmation-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeModal() {
    const modal = document.getElementById('confirmation-modal') as HTMLDialogElement;
    modal.close();
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
