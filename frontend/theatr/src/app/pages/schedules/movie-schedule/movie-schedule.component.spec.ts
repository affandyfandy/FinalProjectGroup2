import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieScheduleComponent } from './movie-schedule.component';

describe('MovieScheduleComponent', () => {
  let component: MovieScheduleComponent;
  let fixture: ComponentFixture<MovieScheduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovieScheduleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MovieScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
