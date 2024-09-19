import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncateName',
  standalone: true
})
export class TruncateNamePipe implements PipeTransform {

  transform(value: string): string {
    if (!value) return value;

    const firstName = value.split(' ')[0];

    return firstName.length > 12 ? firstName.substring(0, 12) : firstName;
  }

}
