import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { AddScheduleDTO, Schedule } from '../../../model/schedule.model';
import { Movie } from '../../../model/movie.model';
import { Studio } from '../../../model/studio.model';
import { TimeFormatPipe } from '../../../core/pipes/time-format/time-format.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TimeFormatPipe,
    PriceFormatPipe
  ],
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.css'
})
export class ScheduleComponent implements OnInit {

  @ViewChild('scheduleForm') scheduleForm: NgForm = {} as NgForm;

  currentDateTime = '';

  scheduleList: Schedule[] = [];
  movieList: Movie[] = [];
  studioList: Studio[] = [];
  availableShowTime: Date[] = [];

  currentMovie: Movie = {} as Movie;
  currentStudio: Studio = {} as Studio;
  currentSchedule: AddScheduleDTO = {} as AddScheduleDTO;
  currentScheduleDateTime = '';

  constructor() { }

  ngOnInit(): void {
    this.currentDateTime = this.getTodayDate();
    this.getScheduleList();
  }

  getScheduleList() {
    // call API to get schedule list
    const date = new Date();
    const [year, month, day] = this.currentDateTime.split('-').map(Number);

    date.setFullYear(year);
    date.setMonth(month - 1);
    date.setDate(day);
    date.setHours(16, 0, 0, 0);

    for (let i = 1; i < 10; i++) {
      this.scheduleList.push({
        id: 'id' + 1,
        price: 40000,
        showTime: date,
        movieId: i,
        movie: {
          title: 'Inside Out 2',
          year: 2024
        },
        studio: {
          name: 'Studio ' + i
        }
      });
    }
  }

  onScheduleTimeChange(event: any) {
    this.currentScheduleDateTime = event.target.value;
    this.getPrice(event.target.value);
    this.getAvailableShowTime();
  }

  getPrice(dateTime: string) {
    const [year, month, day] = dateTime.split('-').map(Number);

    const date = new Date(year, month - 1, day);

    const showDay = date.getDay();

    if (showDay === 0 || showDay === 6) {
      this.currentSchedule.price = 50000;
    } else {
      this.currentSchedule.price = 40000;
    }
  }


  getAvailableShowTime() {
    this.availableShowTime = [];

    for (let hour = 13; hour <= 18; hour++) {
      const date = new Date();
      const [year, month, day] = this.currentDateTime.split('-').map(Number);

      date.setFullYear(year);
      date.setMonth(month - 1);
      date.setDate(day);
      date.setHours(hour, 0, 0, 0);

      const isOccupied = this.scheduleList.some((schedule: Schedule) => {
        const scheduleDate = new Date(schedule.showTime ?? '');
        return (
          schedule.studioId === this.currentStudio.id &&
          scheduleDate.getFullYear() === date.getFullYear() &&
          scheduleDate.getMonth() === date.getMonth() &&
          scheduleDate.getDate() === date.getDate() &&
          scheduleDate.getHours() === date.getHours()
        );
      });

      if (!isOccupied) {
        this.availableShowTime.push(date);
      }
    }
  }

  getMovieList() {
    // call API to get movie list
    for (let i = 1; i < 10; i++) {
      this.movieList.push({
        id: 'id' + i,
        title: 'Inside Out ' + i,
        year: 2020 + i
      });
    }
  }

  getStudioList() {
    // call API to get studio list
    for (let i = 1; i < 6; i++) {
      this.studioList.push({
        id: i,
        name: 'Studio ' + i
      });
    }
  }

  showModal() {
    this.currentSchedule = {
      showTime: new Date(),
      movieId: '',
      studioId: 0,
      price: 0
    };

    this.getMovieList();
    this.getStudioList();

    setTimeout(() => {
      const modal = document.getElementById('schedule-modal') as HTMLDialogElement;
      modal.showModal();
    }, 500);
  }

  getTodayDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  closeModal() {
    this.scheduleForm.resetForm();
    this.currentMovie = {} as Movie;
    this.currentStudio = {} as Studio;
    const modal = document.getElementById('schedule-modal') as HTMLDialogElement;
    this.currentSchedule = {} as AddScheduleDTO;
    modal.close();
  }

  onDateChange(event: any) {
    this.currentDateTime = event.target.value;
    console.log("Tanggal dipilih: ", this.currentDateTime);
    this.getScheduleList();
  }

  isSaveButtonDisabled(): boolean {
    return this.currentScheduleDateTime === '';
  }
}
