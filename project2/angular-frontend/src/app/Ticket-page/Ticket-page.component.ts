import { SharedModule } from './../shared/shared.module';
import {
  CustomerService
} from './../customer.service';


import {
  MediaMatcher
} from '@angular/cdk/layout';
import {
  ChangeDetectorRef,
  OnDestroy,
  Inject
} from '@angular/core';
import {
  Component,
  OnInit
} from '@angular/core';
import {
  MatDialog
} from '@angular/material';


import {
  ApiService
} from '../api.service';
import {
  EventAmount
} from '../model/EventAmount';
import {
  Router
} from '@angular/router';


@Component({
  selector: 'app-Ticket-page',
  templateUrl: './Ticket-page.component.html',
  styleUrls: ['./Ticket-page.component.css']
})
export class TicketPageComponent implements OnInit, OnDestroy {
  mobileQuery: MediaQueryList;


  private _mobileQueryListener: () => void;
  title = this.app.title;
  customer = this.c;
  message: string;
  tickets: EventAmount[] = [];
  // tslint:disable-next-line:max-line-length
  constructor(private router: Router, private api: ApiService, private app: SharedModule, private c: CustomerService, public dialog: MatDialog, changeDetectorRef: ChangeDetectorRef, media: MediaMatcher) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
    this.getEvents();
  }
  getEvents(): void {
    this.api.getTickets().subscribe(r => {
      if (r != undefined) {

        var c = 0
        r.forEach(element => {
          element.id = '' + c++
          element.title = element.title.concat(" x" + element.amount);
        });
        this.tickets = r;
      }
    });
    //.subscribe(clientiArray => this.events = clientiArray);
    // this.events=this.eventsService.getEvents();
  }

  logout(): void {
    this.api.logoutOrganizer();
  }



  ngOnInit() {}
  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  goToStore = function () {
    this.router.navigateByUrl('/store');
  };
}
