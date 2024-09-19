import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { AddScheduleDTO, Schedule } from '../../../model/schedule.model';
import { Movie } from '../../../model/movie.model';
import { Studio } from '../../../model/studio.model';
import { TimeFormatPipe } from '../../../core/pipes/time-format/time-format.pipe';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TimeFormatPipe
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

  currentSchedule: AddScheduleDTO = {} as AddScheduleDTO;
  tempSchedule: AddScheduleDTO = {} as AddScheduleDTO;

  constructor() { }

  ngOnInit(): void {
    this.getScheduleList();
    this.currentDateTime = this.getTodayDate();
  }

  getScheduleList() {
    // call API to get schedule list
    for (let i = 1; i < 10; i++) {
      this.scheduleList.push({
        id: 'id' + 1,
        price: 40000,
        showTime: new Date(),
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

  showModal(schedule?: Schedule) {
    this.currentSchedule = {
      showTime: schedule?.showTime || new Date(),
      movieId: schedule?.movie?.id || '',
      studioId: schedule?.studio?.id || 0,
    };

    const modal = document.getElementById('schedule-modal') as HTMLDialogElement;
    modal.showModal();
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
    const modal = document.getElementById('schedule-modal') as HTMLDialogElement;
    this.tempSchedule = {} as AddScheduleDTO;
    modal.close();
  }

  onDateChange(event: any) {
    this.currentDateTime = event.target.value;
    console.log("Current Date: ", this.currentDateTime);
  }
}
