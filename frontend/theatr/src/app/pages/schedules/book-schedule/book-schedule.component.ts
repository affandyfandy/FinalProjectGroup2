import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { BookingScheduleResponse, Schedule } from '../../../model/schedule.model';
import { Seat } from '../../../model/seat.model';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { User } from '../../../model/user.model';
import { UserService } from '../../../services/user/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageConstants, RouterConfig } from '../../../config/app.constants';
import { BookingService } from '../../../services/booking/booking.service';
import { ScheduleService } from '../../../services/schedule/schedule.service';
import { CreateBookingDTO } from '../../../model/booking.model';

@Component({
  selector: 'app-book-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FullDateTimePipe,
    PriceFormatPipe
  ],
  providers: [
    UserService,
    BookingService,
    ScheduleService
  ],
  templateUrl: './book-schedule.component.html'
})
export class BookScheduleComponent implements OnInit {

  scheduleData: BookingScheduleResponse = {};
  userData: User = {};

  selectedSeats: Seat[] = [];

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  isLoading = false;
  isBookingLoading = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
    private bookingService: BookingService,
    private scheduleService: ScheduleService
  ) { }

  ngOnInit(): void {
    this.getUserData();
    this.getScheduleData();
  }

  getScheduleData() {
    this.isLoading = true;
    const scheduleId = this.route.snapshot.paramMap.get('id');
    if (!!scheduleId) {
      this.scheduleService.getScheduleDetailSeats(scheduleId).subscribe({
        next: (res: any) => {
          this.scheduleData = res;
          this.isLoading = false;
        },
        error: (err) => {
          this.showAlert(MessageConstants.GET_SCHEDULE_FAILED(err), false);
          this.isLoading = false;
        }
      });
    }
  }

  private getUserData() {
    this.userService.getProfile().subscribe({
      next: (res: any) => {
        this.userData = res;
      },
      error: (err) => {
        this.showAlert(MessageConstants.GET_PROFILE_FAILED(err), false);
      }
    });
  }

  isBalanceNotEnough(): boolean {
    return (this.userData.balance ?? 0) < (this.scheduleData.price ?? 0) * this.selectedSeats.length;
  }

  onSeatClick(seat: Seat) {
    if (seat.status === 'AVAILABLE') {
      const index = this.selectedSeats.findIndex(selectedSeat => selectedSeat.id === seat.id);
      if (index >= 0) {
        this.selectedSeats.splice(index, 1);
      } else {
        this.selectedSeats.push(seat);
      }
    }
  }

  isSeatSelected(seat: Seat): boolean {
    return this.selectedSeats.some(selectedSeat => selectedSeat.id === seat.id);
  }

  showModal() {
    const modal = document.getElementById('booking-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeModal() {
    const modal = document.getElementById('booking-modal') as HTMLDialogElement;
    modal.close();
  }

  bookSchedule() {
    if (this.isBalanceNotEnough()) {
      this.navigateToProfile();
    } else {
      this.createBooking();
    }
  }

  private createBooking() {
    this.isBookingLoading = true;
    const body: CreateBookingDTO = {
      scheduleIds: [this.scheduleData.scheduleId ?? ''],
      seatIds: this.selectedSeats.map(seat => seat.id),
      totalAmount: (this.scheduleData.price ?? 0) * this.selectedSeats.length
    };

    this.bookingService.createBooking(body).subscribe({
      next: (res: any) => {
        this.isBookingLoading = false;
        this.showAlert(MessageConstants.CREATE_BOOKING_SUCCESS, true);
        this.closeModal();
        setTimeout(() => {
          this.navigateToHistory();
        }, 3000);
      },
      error: (err) => {
        this.isBookingLoading = false;
        this.closeModal();
        this.showAlert(MessageConstants.CREATE_BOOKING_FAILED(err), false);
      }
    });
  }

  private navigateToProfile() {
    this.router.navigate([RouterConfig.PROFILE.link]);
  }

  private navigateToHistory() {
    this.router.navigate([RouterConfig.BOOKINGS.link]);
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
