import { CustomerService } from './../customer.service';


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


import { ApiService } from '../api.service';
import { SharedModule } from '../shared/shared.module';


@Component({
  selector: 'app-main-content',
  templateUrl: './main-content.component.html',
  styleUrls: ['./main-content.component.css'],

})
export class MainContentComponent implements OnInit, OnDestroy {
  mobileQuery: MediaQueryList;

  

  private _mobileQueryListener: () => void;
  title = this.app.title;
  customer = this.c;
  message: string;
  // tslint:disable-next-line:max-line-length
  constructor(private api: ApiService, private app: SharedModule, private c: CustomerService, public dialog: MatDialog, changeDetectorRef: ChangeDetectorRef, media: MediaMatcher) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
    this.getEvents();
    console.log("costruttore main component")
  }
getEvents(): void {
    this.api.getOwnedEvents();
   //.subscribe(clientiArray => this.events = clientiArray);
    // this.events=this.eventsService.getEvents();
  }

  logout(): void {
    this.api.logoutOrganizer();
  }



  ngOnInit() { }
  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

}
