import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookScheduleComponent } from './book-schedule.component';

describe('BookScheduleComponent', () => {
  let component: BookScheduleComponent;
  let fixture: ComponentFixture<BookScheduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookScheduleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
