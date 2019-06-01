import {
  Injectable, OnInit
} from '@angular/core';
import {
  Event
} from './event';
import {
  EventAmount
} from './model/EventAmount';

const TOKEN = 'TOKEN';
const ID = "ID";
const EMAIL = "EMAIL";
const USER = "USER";
const PHONE = "PHONE";
const NAME = "NAME";
const SURNAME = "SURNAME";
const IS_USER = "IS_USER";
const IS_ORGANIZER = "IS_ORGANIZER";


@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  events: Event[] = [];
  position: Position;
  latitude:Number=39;
  longitude:Number=16;
  rangeTime: Number=3;
  rangeDistance: Number=4;

  constructor() {
  
    console.log('Check position...');


    if (window.navigator && window.navigator.geolocation) {
      window.navigator.geolocation.getCurrentPosition(
        position => {
          this.position = position,
          this.latitude=position.coords.latitude,
          this.longitude=position.coords.longitude
            // console.log(position)
        },
        error => {
          switch (error.code) {
            case 1:
              console.log('Permission Denied');
              break;
            case 2:
              console.log('Position Unavailable');
              break;
            case 3:
              console.log('Timeout');
              break;
          }
        }
      );
    }
    else{
      console.log('Permission Denied');

    };

  }
  setIsOrganizer(): any {
    localStorage.setItem(IS_ORGANIZER, "true");
    localStorage.removeItem(IS_USER);

  }
  setIsUser(): any {
    localStorage.setItem(IS_USER, "true");
    localStorage.removeItem(IS_ORGANIZER);

  }

  logout() {
    localStorage.removeItem(ID);
    localStorage.removeItem(EMAIL);
    localStorage.removeItem(USER);
    localStorage.removeItem(PHONE);
    localStorage.removeItem(IS_USER);
    localStorage.removeItem(IS_ORGANIZER);
  }


  getEvents(): Event[] {
    return this.events;
  }
  setToken(token: string): void {
    localStorage.setItem(TOKEN, token);
  }
  removeToken(): void {
    localStorage.removeItem(TOKEN);
  }
  getToken(): string {
    return localStorage.getItem(TOKEN);
  }
  isUser() {

    return localStorage.getItem(IS_USER) != null;
  }

  isOrganizer() {

    return localStorage.getItem(IS_ORGANIZER) != null;
  }

  isLogged() {

    return localStorage.getItem(TOKEN) != null;
  }
  setEmail(email: string): void {
    localStorage.setItem(EMAIL, email);
  }

  setName(name: string): void {
    localStorage.setItem(NAME, name);
  }

  setSurname(surname: string): void {
    localStorage.setItem(SURNAME, surname);
  }

  setUser(user: string): void {
    localStorage.setItem(USER, user);
  }

  setId(id: string): void {
    localStorage.setItem(ID, id);
  }

  setPhone(phone: string): void {
    localStorage.setItem(PHONE, phone);
  }

  getEmail(): string {
    return localStorage.getItem(EMAIL);
  }

  getUser(): string {
    return localStorage.getItem(USER);
  }

  getId(): string {
    return localStorage.getItem(ID);
  }

  getPhone(): string {
    return localStorage.getItem(PHONE);
  }

  getName(): string {
    return localStorage.getItem(NAME);
  }

  getSurname(): string {
    return localStorage.getItem(SURNAME);
  }

}
