import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DetailSchedule, Schedule } from '../../../model/schedule.model';
import { FormsModule } from '@angular/forms';
import { TimeFormatPipe } from '../../../core/pipes/time-format/time-format.pipe';
import { Movie } from '../../../model/movie.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PriceFormatPipe } from '../../../core/pipes/price-format/price-format.pipe';
import { RouterConfig } from '../../../config/app.constants';
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

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private scheduleService: ScheduleService
  ) { }

  ngOnInit(): void {
    this.currentDateTime = this.getTodayDate();
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
    const movieId = this.route.snapshot.paramMap.get('id');
    this.scheduleService.getScheduleByMovieId(movieId!, 0, 10, this.currentDateTime).subscribe({
      next: (res: any) => {
        this.movie = res?.content[0] ?? [];
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }

  getScheduleList(page: number = 0) {
    const movieId = this.route.snapshot.paramMap.get('id');
    this.scheduleService.getScheduleByMovieId(movieId!, page, 10, this.currentDateTime).subscribe({
      next: (res: any) => {
        this.detailSchedule = res?.content ?? [];
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }

  navigateToBook(scheduleId: string) {
    this.router.navigate([RouterConfig.SCHEDULES.path, scheduleId, 'book']);
  }

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }
}
