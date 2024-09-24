import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Booking } from '../../../model/booking.model';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';

@Component({
  selector: 'app-history-detail',
  standalone: true,
  imports: [
    CommonModule,
    PriceFormatPipe,
    FullDateTimePipe
  ],
  templateUrl: './history-detail.component.html'
})
export class HistoryDetailComponent implements OnInit {

  bookingData: Booking = {};

  constructor() { }

  ngOnInit(): void {
    this.getBookingData();
  }

  private getBookingData() {
    // Change this to get the booking data from the backend
    this.bookingData = {
      id: '1',
      totalAmount: 80000,
      updatedTime: new Date(),
      user: {
        id: '1',
        name: 'User 1'
      },
      seats: [
        {
          id: 1,
          seatCode: 'A1',
        },
        {
          id: 2,
          seatCode: 'A2',
        }
      ],
      isPrinted: false,
      schedule: {
        id: '1',
        movie: {
          id: '1',
          title: 'Inside Out',
          year: 2015,
          posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
          synopsis: "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone."
        },
        showDate: new Date(),
        price: 40000,
        studio: {
          name: 'Studio 1',
        }
      }
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
}
