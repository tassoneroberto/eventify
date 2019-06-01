import { TestBed } from '@angular/core/testing';
import { Event } from './../event';
import {
  CustomerService
} from './../customer.service';
import {
  Component,
  OnInit,
  Input,
  IterableDiffers
} from '@angular/core';

import {
  ChangeDetectionStrategy,
  ViewChild,
  TemplateRef,
} from '@angular/core';
import {
  startOfDay,
  endOfDay,
  subDays,
  addDays,
  endOfMonth,
  isSameDay,
  isSameMonth,
  addHours
} from 'date-fns';
import {
  Subject, from
} from 'rxjs';
import {
  NgbModal
} from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  CalendarView
} from 'angular-calendar';
import {
  getEventsInPeriod
} from 'calendar-utils';
import {
  colors
} from '../calendarHeader/color';


@Component({
  selector: 'calendar-component',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['calendar.component.css'],
  templateUrl: 'calendar.component.html'
})
export class CalendarComponent implements OnInit {
  @Input() eventList: Event[];
  differ: any;

  constructor(private modal: NgbModal, private customer: CustomerService,differs: IterableDiffers) {
    this.differ = differs.find([]).create(null);

  }

  ngOnInit(): void {
    if(this.eventList==undefined||this.eventList==null||this.eventList==[])return;

    this.eventList.forEach(e => {
      this.events.push({
        title: e.title,
        start: e.opening,
        end: e.ending,
        color: colors.red,
        draggable: false,
        resizable: {
          beforeStart: false,
          afterEnd: false
        }
      });
      this.refresh.next();
    });
  }
  ngDoCheck() {
    if(this.eventList==undefined||this.eventList==null||this.eventList==[])return;

    const change = this.differ.diff(this.eventList);
    if(change){
      this.events=[];
    // here you can do what you want on array change
    // you can check for forEachAddedItem or forEachRemovedItem on change object to see the added/removed items
    // Attention: ngDoCheck() is triggered at each binded variable on componenet; if you have more than one in your component, make sure you filter here the one you want.
    this.eventList.forEach(e => {
      this.events.push({
        title: e.title,
        start: e.opening,
        end: e.ending,
        color: colors[e.category],
        draggable: false,
        resizable: {
          beforeStart: false,
          afterEnd: false
        }
      });
      this.refresh.next();
    });
  }
  }
  @ViewChild('modalContent')
  modalContent: TemplateRef < any > ;

  view: CalendarView = CalendarView.Month;

  CalendarView = CalendarView;

  viewDate: Date = new Date();

  modalData: {
    action: string;
    event: CalendarEvent;
  };

  actions: CalendarEventAction[] = [{
      label: '<i class="fa fa-fw fa-pencil"></i>',
      onClick: ({
        event
      }: {
        event: CalendarEvent
      }): void => {
        this.handleEvent('Edited', event);
      }
    },
    {
      label: '<i class="fa fa-fw fa-times"></i>',
      onClick: ({
        event
      }: {
        event: CalendarEvent
      }): void => {
        this.events = this.events.filter(iEvent => iEvent !== event);
        this.handleEvent('Deleted', event);
      }
    }
  ];

  refresh: Subject < any > = new Subject();

  events: CalendarEvent[] = [];
  activeDayIsOpen: boolean = true;


  dayClicked({
    date,
    events
  }: {
    date: Date;events: CalendarEvent[]
  }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.viewDate = date;
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
    }
  }

  eventTimesChanged({
    event,
    newStart,
    newEnd
  }: CalendarEventTimesChangedEvent): void {
    event.start = newStart;
    event.end = newEnd;
    this.handleEvent('Dropped or resized', event);
    this.refresh.next();
  }

  handleEvent(action: string, event: CalendarEvent): void {
    this.modalData = {
      event,
      action
    };
    this.modal.open(this.modalContent, {
      size: 'lg'
    });
  }

  addEvent(): void {
    this.events.push({
      title: 'New event',
      start: startOfDay(new Date()),
      end: endOfDay(new Date()),
      color: colors.red,
      draggable: true,
      resizable: {
        beforeStart: true,
        afterEnd: true
      }
    });
    this.refresh.next();
  }
}
