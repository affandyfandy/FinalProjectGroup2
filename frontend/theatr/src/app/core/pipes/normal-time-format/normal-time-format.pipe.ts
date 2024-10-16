import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'normalTimeFormat',
  standalone: true
})
export class NormalTimeFormatPipe implements PipeTransform {

  transform(value: Date | string): string {
    if (!value) return '';

    const date = new Date(value);
    const hours = (date.getHours()).toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${hours}:${minutes}`;
  }

}
