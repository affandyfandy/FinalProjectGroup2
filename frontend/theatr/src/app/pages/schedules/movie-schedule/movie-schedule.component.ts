import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Schedule } from '../../../model/schedule.model';
import { FormsModule } from '@angular/forms';
import { TimeFormatPipe } from '../../../core/pipes/time-format/time-format.pipe';
import { Movie } from '../../../model/movie.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { RouterConfig } from '../../../config/app.constants';

@Component({
  selector: 'app-movie-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TimeFormatPipe,
    PriceFormatPipe
  ],
  templateUrl: './movie-schedule.component.html',
  styleUrl: './movie-schedule.component.css'
})
export class MovieScheduleComponent implements OnInit {

  scheduleList: Schedule[] = [];
  movie: Movie = {};
  currentDateTime = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.getMovieData();
    this.currentDateTime = this.getTodayDate();
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

  getMovieData() {
    this.movie = {
      id: this.route.snapshot.paramMap.get('id') ?? '',
      title: 'Inside Out 2',
      year: 2020,
      posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
      synopsis: "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone."
    }
  }

  getScheduleList() {
    // Change this to get the list of schedules from the backend
    this.scheduleList = [];
    for (let i = 0; i < 6; i++) {
      const date = new Date();
      date.setHours(13 + i);
      date.setMinutes(0);
      this.scheduleList.push({
        id: i.toString(),
        price: 40000,
        movie: {
          id: i.toString(),
          title: 'Inside Out 2',
          year: 2020,
          posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
          synopsis: "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone."
        },
        showTime: date,
        studio: {
          id: i,
          name: 'Studio ' + i,
        }
      });
    }
  }

  navigateToBook(scheduleId: string) {
    this.router.navigate([RouterConfig.SCHEDULES.path, scheduleId, 'book']);
  }
}
