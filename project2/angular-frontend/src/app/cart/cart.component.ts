import {
  EventAmount
} from './../model/EventAmount';
import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';
import {
  CartService
} from '../cart.service';
import {
  Subscription
} from 'rxjs';
import {
  EventAction
} from 'calendar-utils';
import {
  StoreComponent
} from '../store/store.component';
import {
  MatSnackBar
} from '@angular/material';
import { Router } from '@angular/router';

const OFFSET_HEIGHT: number = 170
const event_HEIGHT: number = 48

@Component({
  selector: 'cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  events: EventAmount[] = []
  numevents: number = 0
  animatePlop: boolean = false
  animatePopout: boolean = false
  expanded: boolean = false
  expandedHeight: string
  cartTotal: number = 0


  changeDetectorRef: ChangeDetectorRef


  constructor(private router: Router,private snackBar: MatSnackBar, private store: StoreComponent, private cartService: CartService, changeDetectorRef: ChangeDetectorRef) {
    this.changeDetectorRef = changeDetectorRef
  }

  ngOnInit() {
    this.loadCartComponent();

  }

  loadCartComponent() {
    this.expandedHeight = '0'
    this.cartService.eventAdded$.subscribe(data => {
      this.events = data.events
      this.cartTotal = data.cartTotal
      this.numevents = data.events.reduce((acc, event) => {
        acc += event.amount
        return acc
      }, 0)

      //Make a plop animation
      if (this.numevents > 1) {
        this.animatePlop = true
        setTimeout(() => {
          this.animatePlop = false
        }, 160)
      } else if (this.numevents == 1) {
        this.animatePopout = true
        setTimeout(() => {
          this.animatePopout = false
        }, 300)
      }
      this.expandedHeight = (this.events.length * event_HEIGHT + OFFSET_HEIGHT) + 'px'
      if (!this.events.length) {
        this.expanded = false
      }
      this.changeDetectorRef.detectChanges()
    })
  }
  deleteevent(event) {
    this.cartService.deleteeventFromCart(event)
  }

  onCartClick() {
    this.expanded = !this.expanded
  }
  flushCart() {
    this.expanded = !this.expanded;
    this.cartService.flushCart().subscribe(r => {

      if (r.message == "Success") {
        this.snackBar.open(r.message, "", {
          duration: 1000
        });
        this.router.navigateByUrl('/tickets');

      } else {
        this.snackBar.open(r.message, "", {
          duration: 1000
        });
        this.cartService.loadCart();
      }
    });
  }
}
