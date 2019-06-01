import 'flatpickr/dist/flatpickr.css'; // you may need to adjust the css import depending on your build tool
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';

import { CalendarComponent } from './calendar.component';

import { CommonModule } from '@angular/common';

import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbModalModule,
    FlatpickrModule.forRoot(),
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory
    })
  ],
  declarations: [CalendarComponent],
  exports: [CalendarComponent]
})
export class DemoModule {}
