import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Schedule } from '../../model/schedule.model';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterConfig } from '../../config/app.constants';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  currentDateTime = '';

  slidingBanner: Schedule[] = []
  scheduleList: Schedule[] = []

  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentDateTime = this.getTodayDate();
    this.getSlidingBanner();
    this.getScheduleList();
  }

  getTodayDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  onDateChange(event: any) {
    this.currentDateTime = event.target.value;
    this.getScheduleList();
  }

  getSlidingBanner() {
    // Change this to get the list of schedules from the backend
    for (let i = 0; i < 5; i++) {
      this.slidingBanner.push({
        id: i.toString(),
        movie: {
          id: i.toString(),
          title: 'Inside Out ' + i,
          year: 2015,
          posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
          synopsis: "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone."
        },
        showTime: new Date()
      });
    }
  }

  getScheduleList() {
    // Change this to get the list of schedules from the backend
    for (let i = 0; i < 6; i++) {
      this.scheduleList.push({
        id: i.toString(),
        movie: {
          id: i.toString(),
          title: 'Inside Out ' + i,
          year: 2015 + i,
          posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
          synopsis: "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone."
        },
        showTime: new Date()
      });
    }
  }

  navigateToSchedule(id: string) {
    this.router.navigate([RouterConfig.SCHEDULES.path, id]);
  }
}
