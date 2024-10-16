import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { AddScheduleDTO, CreateScheduleDTO, Schedule } from '../../../model/schedule.model';
import { Movie } from '../../../model/movie.model';
import { Studio } from '../../../model/studio.model';
import { TimeFormatPipe } from '../../../core/pipes/time-format/time-format.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { MovieService } from '../../../services/movie/movie.service';
import { StudioService } from '../../../services/studio/studio.service';
import { ScheduleService } from '../../../services/schedule/schedule.service';
import { NormalTimeFormatPipe } from '../../../core/pipes/normal-time-format/normal-time-format.pipe';
import { MessageConstants } from '../../../config/app.constants';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TimeFormatPipe,
    PriceFormatPipe,
    NormalTimeFormatPipe
  ],
  providers: [
    MovieService,
    StudioService,
    ScheduleService
  ],
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.css'
})
export class ScheduleComponent implements OnInit {

  @ViewChild('scheduleForm') scheduleForm: NgForm = {} as NgForm;

  currentDateTime = '';

  scheduleList: Schedule[] = [];
  tempScheduleList: Schedule[] = [];
  movieList: Movie[] = [];
  studioList: Studio[] = [];
  availableShowTime: Date[] = [];

  currentMovie: Movie = {};
  currentStudio: Studio = {};
  currentSchedule: AddScheduleDTO = {
    showDate: new Date(),
    movieId: [],
    studioId: [],
    price: 0
  };
  currentScheduleDateTime = '';

  currentPage = 1;
  totalPages = 1;
  sortDir = "DESC";

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  isListLoading = false;
  isSaveLoading = false;

  constructor(
    private movieService: MovieService,
    private studioService: StudioService,
    private scheduleService: ScheduleService
  ) { }

  ngOnInit(): void {
    this.currentDateTime = this.getTodayDate();
    this.getMovieList();
    this.getStudioList();
    this.getScheduleList();
  }

  getScheduleList(page: number = 0) {
    this.isListLoading = true;
    this.scheduleList = [];
    this.scheduleService.getAllSchedules(this.currentDateTime, page, 10, this.sortDir).subscribe({
      next: (res: any) => {
        this.scheduleList = res.content;
        this.currentPage = page;
        this.totalPages = res.totalPages;
        this.isListLoading = false;
      },
      error: (err) => {
        this.showAlert(MessageConstants.GET_SCHEDULE_LIST_FAILED(err), false);
        this.isListLoading = false;
      },
      complete: () => {
        this.isListLoading = false;
      }
    });
  }

  getTempScheduleList() {
    this.tempScheduleList = [];
    this.scheduleService.getAllSchedules(this.currentDateTime, 0, 1000).subscribe({
      next: (res: any) => {
        this.tempScheduleList = res.content;
        if (!!this.currentStudio.id) {
          this.getAvailableShowTime();
        }
      },
      error: (err) => {
        this.showAlert(MessageConstants.GET_SCHEDULE_LIST_FAILED(err), false);
      }
    });
  }

  createSchedule() {
    this.isSaveLoading = true;
    this.currentSchedule.movieId.push(this.currentMovie.id!);
    this.currentSchedule.studioId.push(this.currentStudio.id!);

    const dto: CreateScheduleDTO = {
      showDate: this.formatDateTime(this.currentSchedule.showDate),
      movieId: this.currentSchedule.movieId,
      studioId: this.currentSchedule.studioId,
      price: this.currentSchedule.price
    }
    this.scheduleService.createSchedule(dto).subscribe({
      next: (res: any) => {
        this.closeModal();
        this.getScheduleList();
        this.showAlert(MessageConstants.CREATE_SCHEDULE_SUCCESS, true);
        this.isSaveLoading = false;
      },
      error: (err) => {
        this.showAlert(MessageConstants.CREATE_SCHEDULE_FAILED(err), false);
        this.isSaveLoading = false;
      },
      complete: () => {
        this.isSaveLoading = false;
      }
    });
  }

  formatDateTime(date: Date): string {
    const pad = (n: number) => n < 10 ? '0' + n : n;

    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T` +
      `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
  }

  onScheduleTimeChange(event: any) {
    this.currentScheduleDateTime = event.target.value;
    this.getTempScheduleList();
    this.getPrice(event.target.value);
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


  getAvailableShowTime(event?: Event) {
    this.getPrice(this.currentScheduleDateTime);
    if (!!event) {
      const selectedStudioId = ((event!.target as HTMLSelectElement).value);
      this.currentStudio = this.studioList.find(studio => studio.id === Number(selectedStudioId)) || ({} as Studio);
    }

    this.availableShowTime = [];

    for (let hour = 12; hour <= 16; hour++) {
      const date = new Date();
      const [year, month, day] = this.currentScheduleDateTime.split('-').map(Number);

      date.setFullYear(year);
      date.setMonth(month - 1);
      date.setDate(day);
      date.setHours(hour, 0, 0, 0);

      const isOccupied = this.tempScheduleList.some((schedule: Schedule) => {
        const scheduleDate = new Date(schedule.showDate ?? '');
        scheduleDate.setHours(scheduleDate.getHours() - 7);
        return (
          schedule.studioIds![0].id === this.currentStudio.id &&
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

  getMovieList(page: number = 0) {
    this.movieService.getAllMovies(page, '', 10000).subscribe({
      next: (res: any) => {
        this.movieList = res.content;
        this.currentPage = page;
        this.totalPages = res.totalPages;
      },
      error: (err) => {
        this.showAlert(MessageConstants.GET_MOVIE_LIST_FAILED(err), false);
      }
    });
  }

  getStudioList(page: number = 0) {
    this.studioService.getActiveStudioList().subscribe({
      next: (res: any) => {
        this.studioList = res;
      },
      error: (err) => {
        this.showAlert(MessageConstants.GET_STUDIO_LIST_FAILED(err), false);
      }
    });
  }

  showModal() {
    if (!!this.currentMovie.id) {
      this.getTempScheduleList();
    }
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
    const modal = document.getElementById('schedule-modal') as HTMLDialogElement;
    modal.close();
  }

  onDateChange(event: any) {
    this.currentDateTime = event.target.value;
    this.getScheduleList();
  }

  isSaveButtonDisabled(): boolean {
    return !this.currentMovie.id ||
      !this.currentStudio.id ||
      this.currentSchedule.price === 0 ||
      this.isPastDate() ||
      this.isSaveLoading;
  }

  private isPastDate(): boolean {
    const currentDate = new Date();
    return this.currentSchedule.showDate < currentDate;
  }

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }

  onMoviePickerChange(event: Event) {
    const selectedMovieId = (event.target as HTMLSelectElement).value;
    this.currentMovie = this.movieList.find(movie => movie.id === selectedMovieId) || ({} as Movie);
  }

  onTimePickerChange(event: Event) {
    const selectedTime = (event.target as HTMLSelectElement).value;
    this.currentSchedule.showDate = new Date(selectedTime);
  }

  onOrderPickerChange(event: Event) {
    const selectedOrder = (event.target as HTMLSelectElement).value;
    this.sortDir = selectedOrder;
    this.getScheduleList();
  }
}
