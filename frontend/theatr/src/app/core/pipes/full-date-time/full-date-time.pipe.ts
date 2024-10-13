import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'fullDateTime',
  standalone: true
})
export class FullDateTimePipe implements PipeTransform {

  transform(value: Date | string | null): string {
    if (!value) {
      return '';
    }

    const date = new Date(value);
    date.setHours(date.getHours() - 7);
    const options: Intl.DateTimeFormatOptions = {
      day: 'numeric',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    };

    return new Intl.DateTimeFormat('en-GB', options).format(date);
  }
}
