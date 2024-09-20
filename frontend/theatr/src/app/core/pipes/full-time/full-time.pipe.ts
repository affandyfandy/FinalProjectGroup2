import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'fullTime',
  standalone: true
})
export class FullTimePipe implements PipeTransform {

  transform(value: Date | string | null): string {
    if (!value) {
      return '';
    }

    const date = new Date(value);

    const hours = this.padZero(date.getHours());
    const minutes = this.padZero(date.getMinutes());
    const seconds = this.padZero(date.getSeconds());

    return `${hours}:${minutes}:${seconds}`;
  }

  private padZero(value: number): string {
    return value < 10 ? '0' + value : value.toString();
  }

}
