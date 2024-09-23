import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Schedule } from '../../../model/schedule.model';
import { Seat } from '../../../model/seat.model';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { User } from '../../../model/user.model';
import { UserService } from '../../../services/user/user.service';
import { Router } from '@angular/router';
import { RouterConfig } from '../../../config/app.constants';

@Component({
  selector: 'app-book-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FullDateTimePipe,
    PriceFormatPipe
  ],
  providers: [
    UserService
  ],
  templateUrl: './book-schedule.component.html'
})
export class BookScheduleComponent implements OnInit {

  scheduleData: Schedule = {};
  userData: User = {};

  selectedSeats: Seat[] = [];

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.getUserData();
    this.getScheduleData();
  }

  getScheduleData() {
    // Change this to get the schedule data from the backend
    this.scheduleData = {
      id: '1',
      movie: {
        id: '1',
        title: 'Inside Out',
        year: 2015,
        posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
        synopsis: "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone."
      },
      showTime: new Date(),
      price: 50000,
      studio: {
        name: 'Studio 1',
        seats: this.dummySeats()
      }
    }
  }

  private dummySeats(): Seat[] {
    const seats: Seat[] = [];
    const rows = ['A', 'B', 'C', 'D', 'E'];
    let id = 0;
    for (let i = 0; i < rows.length; i++) {
      for (let j = 1; j < 7; j++) {
        seats.push({
          id: id,
          seatCode: rows[i] + j,
          status: j % 2 === 0 ? 'AVAILABLE' : 'BOOKED'
        });
        id++;
      }
    }
    return seats;
  }

  private getUserData() {
    this.userService.getProfile().subscribe({
      next: (res: any) => {
        this.userData = res;
        console.log(this.userData);
      },
      error: (err) => {
        console.error(err);
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

    }
  }

  private navigateToProfile() {
    this.router.navigate([RouterConfig.PROFILE.link]);
  }
}
