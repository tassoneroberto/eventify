import {
  Event
} from './../event';
export class EventAmount extends Event {
  amount: number;
  constructor(e: Event) {
    super();
    this.amount = 1;
    this.id = e.id;
    this.version = e.version;
    this.title = e.title;
    this.description = e.description;
    this.opening = e.opening;
    this.ending = e.ending;
    this.location = e.location;
    this.latitude = e.latitude;
    this.longitude = e.longitude;
    this.phone = e.phone;
    this.owner_name = e.owner_name;
    this.category = e.category;
    this.total_posts = e.total_posts;
    this.remaining_posts = e.remaining_posts
    this.price = e.price;
  }
}
