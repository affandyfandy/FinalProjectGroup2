import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Schedule } from '../../../model/schedule.model';
import { Seat } from '../../../model/seat.model';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';

@Component({
  selector: 'app-book-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FullDateTimePipe,
    PriceFormatPipe
  ],
  templateUrl: './book-schedule.component.html'
})
export class BookScheduleComponent implements OnInit {

  scheduleData: Schedule = {};

  selectedSeats: Seat[] = [];

  constructor() { }

  ngOnInit(): void {
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
}
