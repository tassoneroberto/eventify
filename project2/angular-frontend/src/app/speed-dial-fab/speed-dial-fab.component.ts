import {
  ApiService
} from './../api.service';
import {
  Event
} from './../event';
import {
  Component,
  OnInit,
  Inject
} from '@angular/core';
import {
  speedDialFabAnimations
} from './spped-dial-fab.animation';
import {
  MatDialogRef,
  MatDialog
} from '@angular/material';

import {
  FormControl
} from '@angular/forms';
import {
  EventsComponent
} from '../events/events.component';

import {
  ViewChild,
  ElementRef,
  NgZone,
  Directive
} from '@angular/core';

import {
  Input
} from '@angular/core';
import {
  MapsAPILoader,
  AgmMap
} from '@agm/core';
import {
  GoogleMapsAPIWrapper
} from '@agm/core/services';

@Component({
  selector: 'app-speed-dial-fab',
  templateUrl: './speed-dial-fab.component.html',
  styleUrls: ['./speed-dial-fab.component.scss'],
  animations: speedDialFabAnimations
})
export class SpeedDialFabComponent {
  fabButtons = [{
      icon: 'create',
      description: "Create new event",
      method: "createEventDialog"
    },
    {
      icon: 'message',
      description: "Send a mail to your followes",
      method: "createEventDialog"
    },
    {
      icon: 'share',
      description: "Share your profile",
      method: "createEventDialog"
    }
  ];
  buttons = [];
  fabTogglerState = 'inactive';

  constructor(public dialog: MatDialog, private api: ApiService) {}

  showItems() {
    this.fabTogglerState = 'active';
    this.buttons = this.fabButtons;
  }

  hideItems() {
    this.fabTogglerState = 'inactive';
    this.buttons = [];
  }

  onToggleFab() {
    this.buttons.length ? this.hideItems() : this.showItems();
  }


  createEventDialog(): void {
    const dialogRef = this.dialog.open(CreateEventDialog, {
      width: '1500px',
      height: '620px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.api.createEvent(result);
    });
  }
}



import {
  EventEmitter,
  Output
} from '@angular/core';
import {
  NgModel
} from '@angular/forms';
import { SharedModule } from '../shared/shared.module';


declare var google: any;

interface Marker {
  lat: number;
  lng: number;
  label ? : string;
  draggable: boolean;
}

interface Location {
  lat: number;
  lng: number;
  viewport ? : Object;
  zoom: number;
  address_level_1 ? : string;
  address_level_2 ? : string;
  address_country ? : string;
  address_zip ? : string;
  address_state ? : string;
  marker ? : Marker;
}




@Component({
  selector: 'create-event-dialog',
  templateUrl: 'create-event-dialog.html',
  styleUrls: ['speed-dial-fab.component.scss'],

})
export class CreateEventDialog implements OnInit {
  geocoder: any;
  public location: Location = {
    lat: 39.366433,
    lng: 16.226275,
    marker: {
      lat: 39.366433,
      lng: 16.226275,
      draggable: true
    },
    zoom: 15
  };

  @ViewChild(AgmMap) map: AgmMap;


  newEvent: Event = new Event();
  categories = this.app.categories;
  lat: number = 43.678418;
  lng: number = -79.809007;


  constructor(public app: SharedModule,
    public dialogRef: MatDialogRef < CreateEventDialog > , public mapsApiLoader: MapsAPILoader,
    private zone: NgZone,
    private wrapper: GoogleMapsAPIWrapper) {
    this.mapsApiLoader = mapsApiLoader;
    this.zone = zone;
    this.wrapper = wrapper;
    this.mapsApiLoader.load().then(() => {
      this.geocoder = new google.maps.Geocoder();
    });
  }


  ngOnInit() {
    this.location.marker.draggable = true;
  }
  onNoClick(): void {
    this.dialogRef.close();
  }
  createClick() {
    this.newEvent.latitude = this.location.lat;
    this.newEvent.longitude = this.location.lng;
    // this.newEvent.location = this.location.address_level_1 + " - " + this.location.address_level_2 + " - " + this.location.address_state + " - " + this.location.address_country;
    console.log(this.newEvent);
  }

  // updateOnMap() {
  //   let full_address:string = this.location.address_level_1 || ""
  //   if (this.location.address_level_2) full_address = full_address + " " + this.location.address_level_2
  //   if (this.location.address_state) full_address = full_address + " " + this.location.address_state
  //   if (this.location.address_country) full_address = full_address + " " + this.location.address_country

  //   this.findLocation(full_address);
  // }
  onSearchChange(searchValue: string) {
    this.findLocation(searchValue);
  }
  findLocation(address) {
    if (!this.geocoder) this.geocoder = new google.maps.Geocoder()
    this.geocoder.geocode({
      'address': address
    }, (results, status) => {
      if (status == google.maps.GeocoderStatus.OK) {


        if (results[0].geometry.location) {
          this.location.lat = results[0].geometry.location.lat();
          this.location.lng = results[0].geometry.location.lng();
          this.location.marker.lat = results[0].geometry.location.lat();
          this.location.marker.lng = results[0].geometry.location.lng();
          this.location.marker.draggable = true;
          this.location.viewport = results[0].geometry.viewport;
        }

        this.map.triggerResize()
      } else {

      }
    })
  }

  findAddressByCoordinates() {
    this.geocoder.geocode({
      'location': {
        lat: this.location.marker.lat,
        lng: this.location.marker.lng
      }
    }, (results, status) => {

      this.newEvent.location = results[0].formatted_address;
      // this.decomposeAddressComponents(results);
    });
  }
  // decomposeAddressComponents(addressArray) {
  //   console.log(addressArray)
  //   if (addressArray.length == 0) return false;
  //   let address = addressArray[0].address_components;

  //   for(let element of address) {
  //     if (element.length == 0 && !element['types']) continue

  //     if (element['types'].indexOf('street_number') > -1) {
  //       this.location.address_level_1 = element['long_name'];
  //       continue;
  //     }
  //     if (element['types'].indexOf('route') > -1) {
  //       this.location.address_level_1 += ', ' + element['long_name'];
  //       continue;
  //     }
  //     if (element['types'].indexOf('locality') > -1) {
  //       this.location.address_level_2 = element['long_name'];
  //       continue;
  //     }
  //     if (element['types'].indexOf('administrative_area_level_1') > -1) {
  //       this.location.address_state = element['long_name'];
  //       continue;
  //     }
  //     if (element['types'].indexOf('country') > -1) {
  //       this.location.address_country = element['long_name'];
  //       continue;
  //     }
  //     if (element['types'].indexOf('postal_code') > -1) {
  //       this.location.address_zip = element['long_name'];
  //       continue;
  //     }
  //   }
  // }
  markerDragEnd(m: any, $event: any) {
    this.location.marker.lat = m.coords.lat;
    this.location.marker.lng = m.coords.lng;
    this.location.lat= this.location.marker.lat;
    this.location.lng=this.location.marker.lng;
    this.findAddressByCoordinates();
  }
}
