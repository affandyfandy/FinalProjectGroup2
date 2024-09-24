import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Schedule } from '../../../model/schedule.model';
import { FullDateTimePipe } from '../../../core/pipes/full-date-time/full-date-time.pipe';
import { Router } from '@angular/router';
import { RouterConfig } from '../../../config/app.constants';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FullDateTimePipe
  ],
  templateUrl: './history.component.html'
})
export class HistoryComponent implements OnInit {

  currentDateTime = '';

  scheduleList: Schedule[] = [];

  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getScheduleList();
  }

  getScheduleList() {
    // Change this to get the list of schedules from the backend
    this.scheduleList = [];
    for (let i = 0; i < 10; i++) {
      this.scheduleList.push({
        id: i.toString(),
        movie: {
          id: i.toString(),
          title: 'Inside Out ' + i,
          year: 2015,
          posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
          synopsis: "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone."
        },
        showTime: new Date(),
        studio: {
          name: 'Studio ' + i,
        },
        booking: {
          id: i.toString(),
          totalAmount: 80000,
          updatedTime: new Date(),
          seats: [
            {
              id: i,
              seatCode: 'A1',
            },
            {
              id: i,
              seatCode: 'A2',
            },
          ]
        }
      });
    }
  }

  onDateChange(event: any) {
    this.currentDateTime = event.target.value;
  }

  navigateToDetail(id: string) {
    this.router.navigate([RouterConfig.BOOKINGS.path, id]);
  }
}
