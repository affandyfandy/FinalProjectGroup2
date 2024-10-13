import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DetailSchedule, Schedule } from '../../../model/schedule.model';
import { FormsModule } from '@angular/forms';
import { TimeFormatPipe } from '../../../core/pipes/time-format/time-format.pipe';
import { Movie } from '../../../model/movie.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { MessageConstants, RouterConfig } from '../../../config/app.constants';
import { ScheduleService } from '../../../services/schedule/schedule.service';

@Component({
  selector: 'app-movie-schedule',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TimeFormatPipe,
    PriceFormatPipe
  ],
  providers: [
    ScheduleService
  ],
  templateUrl: './movie-schedule.component.html',
  styleUrl: './movie-schedule.component.css'
})
export class MovieScheduleComponent implements OnInit {

  detailSchedule: DetailSchedule[] = [];
  movie: DetailSchedule = {};
  currentDateTime = '';

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  isLoading = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private scheduleService: ScheduleService
  ) { }

  ngOnInit(): void {
    this.currentDateTime = this.route.snapshot.paramMap.get('date')!;
    this.getScheduleList();
    this.getMovie();
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

  getMovie() {
    this.isLoading = true;
    const movieId = this.route.snapshot.paramMap.get('id');
    const date = this.route.snapshot.paramMap.get('date');
    this.scheduleService.getScheduleByMovieId(movieId!, 0, 10, date!).subscribe({
      next: (res: any) => {
        this.movie = res?.content[0] ?? [];
        this.isLoading = false;
      },
      error: (err: any) => {
        this.isLoading = false;
        this.showAlert(MessageConstants.GET_MOVIE_FAILED(err), false);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  getScheduleList(page: number = 0) {
    this.isLoading = true;
    const movieId = this.route.snapshot.paramMap.get('id');
    this.scheduleService.getScheduleByMovieId(movieId!, page, 10, this.currentDateTime).subscribe({
      next: (res: any) => {
        this.isLoading = false;
        this.detailSchedule = res?.content ?? [];
      },
      error: (err: any) => {
        this.isLoading = false;
        this.showAlert(MessageConstants.GET_SCHEDULE_LIST_FAILED(err), false);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  navigateToBook(scheduleId: string) {
    this.router.navigate([RouterConfig.SCHEDULES.path, scheduleId, 'customer', 'book']);
  }

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }

  isDisabled(date: Date): boolean {
    var tempDate = new Date(date);
    tempDate.setHours(tempDate.getHours() - 7);
    return tempDate < new Date();
  }
}
