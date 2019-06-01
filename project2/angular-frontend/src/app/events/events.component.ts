import {
  ApiService
}

from './../api.service';

import {
  Component,
  OnInit,
  Input
}

from '@angular/core';

import {
  Event
}

from '../event';

import {
  EVENTS
}

from '../mock-events';

import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogConfig
}

from '@angular/material';

import {
  EditDialog
}

from './edit.component';

import {
  trigger,
  transition,
  query,
  stagger,
  animate,
  style,
  state
}

from '@angular/animations';

import {
  colors
}

from '../calendarHeader/color';

import {
  Observable
}

from 'rxjs';
import { CustomerService } from '../customer.service';
import { MessageDialog } from './mailt.component';

@Component({

    selector: 'app-events',
    templateUrl: './events.component.html',
    styleUrls: ['./events.component.css'],
    animations: [trigger('cardsAnimation', [transition('* => *', [query('mat-card', style({
              transform: 'translateX(-100%)'
            }

          ), {
            optional: true
          }

        ),
        query('mat-card',
          stagger('100ms', [animate('600ms', style({
              transform: 'translateX(0)'
            }

          ))]), { optional: true })
      ])]),

      trigger('myAwesomeAnimation', [state('small', style({
            transform: 'rotateZ(360deg)',
          }

        )),
        state('large', style({
            transform: 'rotateZ(180deg)',
          }

        )),
        transition('* => *', animate('500ms ease-in')),

      ]),

    ]
  }

) export class EventsComponent {
  @Input() eventList:Event[];

  getStyle(category) {

    return colors[category].primary;

  }


  rotatearrow(id:number) {
    this.state[id] = (this.state[id] === 'small' ? 'large' : 'small');
  }

  state: string []= [];

  selectedEvent: Event;

  constructor(private apiService: ApiService, public dialog: MatDialog, private customer:CustomerService) {

    this.state=["small","small"];
  }



  onSelect(event: Event): void {
    this.selectedEvent = event;
  }


  setShareLink(string: string): void {
    string = "";
  }


 

  deleteEvent(id: string): void {
    console.log("removing ", id);
    this.apiService.deleteEvent(id);
  }
sendMessage(event:Event):void{
  const dialogConfig = new MatDialogConfig();


    dialogConfig.data = event;


    const dialogRef = this.dialog.open(MessageDialog,
      dialogConfig);


    dialogRef.afterClosed().subscribe(val => {
      console.log(val);
      console.log(event.id);
      this.apiService.sendMail(val,event.id)});
}
  editEvent(event: Event): void {
    // const dialogConfig = new MatDialogConfig();
    // dialogConfig.data = event;
    // const dialogRef = this.dialog.open(EditDialog,
    //   dialogConfig);
    // dialogRef.afterClosed().subscribe(val => console.log("Dialog output:", val));
      const dialogRef = this.dialog.open(EditDialog, {

        width: '1500px',
        height: '620px',
        data:event
      });
  
      dialogRef.afterClosed().subscribe(result => {
        this.apiService.editEvent(result);
      });
    


  }
}
