
import {
  Event
} from './../event';
import {
  Component,
  OnInit,
  Inject
} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogRef
} from '@angular/material';
import {
  FormControl,
  Validators
} from '@angular/forms';
import {
  MapsAPILoader,
  AgmMap
} from '@agm/core';
import {
  GoogleMapsAPIWrapper
} from '@agm/core/services';
import {
  ViewChild,
  ElementRef,
  NgZone,
  Directive
} from '@angular/core';
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
  selector: 'edit-dialog',
  templateUrl: './edit-dialog.html',
  styleUrls: ['./events.component.css'],
})


export class EditDialog {
  categories = this.app.categories;

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

  constructor(public app:SharedModule,
    public dialogRef: MatDialogRef < EditDialog > ,
    @Inject(MAT_DIALOG_DATA) public event: Event, public mapsApiLoader: MapsAPILoader,
    private zone: NgZone,
    private wrapper: GoogleMapsAPIWrapper) {
    this.mapsApiLoader = mapsApiLoader;
    this.zone = zone;
    this.wrapper = wrapper;
    this.mapsApiLoader.load().then(() => {
      this.geocoder = new google.maps.Geocoder();
    });
   
    this.location.lat = event.latitude;
    this.location.lng = event.longitude;
    this.location.marker.lng = event.longitude;
    this.location.marker.lat = event.latitude;
  }



  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
    this.location.marker.draggable = true;
  }

  editClick() {
    this.event.latitude = this.location.lat;
    this.event.longitude = this.location.lng;
    // this.event.location = this.location.address_level_1 + " - " + this.location.address_level_2 + " - " + this.location.address_state + " - " + this.location.address_country;
 
  }

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
      this.event.location = results[0].formatted_address;
    });
  }

  markerDragEnd(m: any, $event: any) {
    this.location.marker.lat = m.coords.lat;
    this.location.marker.lng = m.coords.lng;
    this.location.lat= this.location.marker.lat;
    this.location.lng=this.location.marker.lng;
    this.findAddressByCoordinates();
  }

}
