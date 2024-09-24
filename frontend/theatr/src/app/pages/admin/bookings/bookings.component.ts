import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Booking } from '../../../model/booking.model';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { FullTimePipe } from '../../../core/pipes/full-time/full-time.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';

@Component({
  selector: 'app-bookings',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FullDateTimePipe,
    FullTimePipe,
    PriceFormatPipe
  ],
  templateUrl: './bookings.component.html'
})
export class BookingsComponent implements OnInit {

  currentDateTime = '';

  bookingList: Booking[] = [];
  currentBooking: Booking = {};

  constructor() { }

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

  getBookingList() {
    // Change this to get the list of bookings from the backend
    const date = new Date();
    const [year, month, day] = this.currentDateTime.split('-').map(Number);
    date.setFullYear(year);
    date.setMonth(month - 1);
    date.setDate(day);
    date.setHours(16, 0, 0, 0);

    for (let i = 0; i < 10; i++) {
      this.bookingList.push({
        id: i.toString(),
        totalAmount: 80000,
        updatedTime: date,
        user: {
          id: i.toString(),
          name: 'User ' + i
        },
        schedule: {
          id: i.toString(),
          showDate: date,
          price: 40000,
          movie: {
            id: i.toString(),
            title: 'Movie ' + i,
            year: 2019 + i,
            posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
          },
          studio: {
            id: i,
            name: 'Studio ' + i
          }
        },
        seats: [
          {
            id: 1,
            seatCode: 'A1',
          },
          {
            id: 2,
            seatCode: 'A2',
          },
        ],
      });
    }
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
}
