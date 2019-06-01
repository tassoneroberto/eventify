import {
  EventAmount
} from './model/EventAmount';
import {
  element
} from 'protractor';
import {
  colors
} from './calendarHeader/color';
import {
  Event
} from './event';
import {
  ApiService
} from './api.service';
import {
  Injectable
} from '@angular/core';
import {
  Subject,
  from
} from 'rxjs';
import {
  MatSnackBar
} from '@angular/material/snack-bar';
import {
  StoreComponent
} from './store/store.component';

@Injectable({
  providedIn: 'root',
})

export class CartService {

  events: EventAmount[] = []
  cartTotal: number = 0

  private eventAddedSource = new Subject < any > ()


  eventAdded$ = this.eventAddedSource.asObservable()

  loadCart() {
    
    this.api.getCart().subscribe(r => {
      r.forEach(element => {
        this.events.push(element);
        this.cartTotal += element.price * element.amount;


      });
      this.eventAddedSource.next({
        events: this.events,
        cartTotal: this.cartTotal
      });
    });
  }
  
  constructor(private api: ApiService, public snackBar: MatSnackBar) {
    console.log("costruttore cart service")
    this.loadCart();
  }

  addeventToCart(event) {
    let max = false;
    this.events.forEach(e => {
      if (e.id == event.id && e.amount == event.remaining_posts) {
        this.snackBar.open("Max amount reached", "", {
          duration: 1000
        });
        max = true;
        return;
      }
    });
    if (max) return
    let exists = false
    //  parseFloat(event.price.replace(/\./g, '').replace(',', '.'))
    //Search this event on the cart and increment the quantity
    this.events = this.events.map(_event => {
      if (_event.id == event.id) {
        _event.amount++
        exists = true
      }
      return _event
    })
    this.cartTotal += event.price;

    //Add a new event to the cart if it's a new event
    if (!exists) {
      this.events.push(
        new EventAmount(event)
      )
    }

    this.eventAddedSource.next({
      events: this.events,
      cartTotal: this.cartTotal
    })
    this.api.addToCart(event);
  }

  deleteeventFromCart(event) {
    this.events = this.events.filter(_event => {
      if (_event.id == event.id) {
        this.cartTotal -= event.price;
        _event.amount--;
        if (_event.amount == 0)
          return false
        else return true
      }
      return true
    })
    this.eventAddedSource.next({
      events: this.events,
      cartTotal: this.cartTotal
    })
    this.api.removeFromCart(event);
  }


  flushCart() {
    var temp = this.events;
    this.events = []
    this.cartTotal = 0
    this.eventAddedSource.next({
      events: this.events,
      cartTotal: this.cartTotal
    })
    return this.api.buy(temp);

  }

}
