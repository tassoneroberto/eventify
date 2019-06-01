import {
  MessageDialog
} from './events/mailt.component';
import {
  StoreComponent
} from './store/store.component';
import {
  HttpModule,
  JsonpModule
} from '@angular/http';
import {
  MatRadioModule
} from '@angular/material/radio';

import {
  SearchBarComponent
} from './search-bar/search-bar.component';
import {
  FiltersComponent
} from './filters/filters.component';
import {
  ShowcaseComponent
} from './showcase/showcase.component';
import {
  CartComponent
} from './cart/cart.component';
import {
  MatSnackBarModule
} from '@angular/material/snack-bar';

import {
  eventThumbnailComponent
} from './event-thumbnail/event-thumbnail.component';
import {
  CartPreviewComponent
} from './cart-preview/cart-preview.component';

import {
  SortFiltersComponent
} from './sort-filters/sort-filters.component';


import {
  CartService
} from './cart.service';

import {
  EditDialog
} from './events/edit.component';

import {
  BrowserModule
} from '@angular/platform-browser';
import {
  NgModule
} from '@angular/core';
import {
  FormsModule
} from '@angular/forms'; // <-- NgModel lives here
import {
  HttpClientModule
} from '@angular/common/http';
import {
  MatListModule
} from '@angular/material/list';
import {
  AppComponent
} from './app.component';
import {
  EventsComponent
} from './events/events.component';

import {
  MatSelectModule
} from '@angular/material/select';
import {
  MatExpansionModule
} from '@angular/material/expansion';

import {
  CalendarModule,
  DateAdapter
} from 'angular-calendar';
import {
  adapterFactory
} from 'angular-calendar/date-adapters/date-fns';
import {
  BrowserAnimationsModule
} from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatNativeDateModule,
  MatCheckboxModule,
  MatSidenavModule
} from '@angular/material';
import {
  MatCardModule
} from '@angular/material/card';
import {
  MatIconModule
} from '@angular/material/icon';
import {
  MatMenuModule
} from '@angular/material/menu';
import {
  MatToolbarModule
} from '@angular/material/toolbar';
import {
  MainContentComponent
} from './main-content/main-content.component';
import {
  MatInputModule
} from '@angular/material/input';
import {
  MatDialogModule
} from '@angular/material/dialog';
import {
  ReactiveFormsModule
} from '@angular/forms';
import {
  MatFormFieldModule
} from '@angular/material/form-field';
import {
  SidebarComponent
} from './sidebar/sidebar.component';

import {
  MatSliderModule
} from '@angular/material/slider';
import {
  MatChipsModule
} from '@angular/material/chips';
import {
  MatTabsModule
} from '@angular/material/tabs';
import {
  MatTooltipModule
} from '@angular/material/tooltip';
import 'hammerjs';
import {
  GooglePlaceModule
} from "ngx-google-places-autocomplete";

import {
  SpeedDialFabComponent,
  CreateEventDialog
} from './speed-dial-fab/speed-dial-fab.component';
import {
  CalendarHeaderComponent
} from './calendarHeader/calendar-header.component';
import {
  AngularFontAwesomeModule
} from 'angular-font-awesome';
import 'core-js/es6/reflect';
import 'core-js/es7/reflect';
import 'zone.js/dist/zone';
import {
  platformBrowserDynamic
} from '@angular/platform-browser-dynamic';

import {
  DemoModule
} from './calendar/module';

import {
  CommonModule
} from '@angular/common';

import {
  FlexLayoutModule
} from '@angular/flex-layout';



import {
  RouterModule,
  Routes
} from '@angular/router';

import {
  LoginPageComponent
} from './login-page/login-page.component';

import {
  NeedAuthGuard
} from './auth.guard';
import {
  SigninPageComponent
} from './signin-page/signin-page.component';

import {
  ShoppingCartModule
} from 'ng-shopping-cart'; // <-- Import the module class


import {
  FlatpickrModule
} from 'angularx-flatpickr';
import {
  TicketPageComponent
} from './Ticket-page/Ticket-page.component';
import {
  UserNeedAuthGuard
} from './user.auth.guard';


import {
  NgbModule
} from '@ng-bootstrap/ng-bootstrap';
import {
  AgmCoreModule,
  GoogleMapsAPIWrapper
} from '@agm/core';
import { SharedModule } from './shared/shared.module';

const appRoutes: Routes = [{
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'tickets',
    component: TicketPageComponent,
    canActivate: [UserNeedAuthGuard]
  },
  {
    path: 'dashboard',
    component: MainContentComponent,
    canActivate: [NeedAuthGuard]
  },
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: 'signin',
    component: SigninPageComponent
  },
  {
    path: 'store',
    component: StoreComponent,
    canActivate: [UserNeedAuthGuard]

  },

];


@NgModule({
  declarations: [LoginPageComponent, SigninPageComponent,
    EventsComponent, CalendarHeaderComponent,
    MainContentComponent,
    EditDialog, CreateEventDialog, MessageDialog,
    SidebarComponent,
    SpeedDialFabComponent,
    AppComponent,
    SearchBarComponent,
    FiltersComponent,
    ShowcaseComponent,
    CartComponent,
    eventThumbnailComponent,
    CartPreviewComponent,
    SortFiltersComponent,
    StoreComponent, TicketPageComponent
  ],
  imports: [
    SharedModule,
    AgmCoreModule.forRoot({
      libraries: ["places"],
      apiKey: 'AIzaSyCFmp4xLNE3ONmxWYX9sc15uOmZ2_NRczU'
    }), // <---
    NgbModule.forRoot(),
    MatRadioModule,
    HttpModule,
    JsonpModule, GooglePlaceModule, BrowserModule, FlatpickrModule.forRoot(),
    AngularFontAwesomeModule,
    MatExpansionModule,
    ShoppingCartModule.forRoot({ // <-- Add the cart module to your root module
      // itemType: MyCartItemClass, // <-- Configuration is optional
      serviceType: 'localStorage',
      serviceOptions: {
        storageKey: 'NgShoppingCart',
        clearOnError: true
      }
    }),
    RouterModule.forRoot(appRoutes),
    ReactiveFormsModule,
    CommonModule,
    RouterModule,
    // Material Modules
    FlexLayoutModule,
    DemoModule,
    BrowserAnimationsModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory
    }), CalendarModule,
    MatListModule,
    MatTabsModule,
    MatFormFieldModule,
    MatSelectModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatTooltipModule,
    MatSliderModule,
    HttpClientModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatInputModule,
    MatMenuModule,
    MatIconModule,
    MatToolbarModule,
    MatDialogModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatSidenavModule,
    MatCheckboxModule,
    MatCardModule,
    FormsModule,
    MatChipsModule,
    MatSnackBarModule,

  ],
  entryComponents: [
    EditDialog,
    CreateEventDialog,
    MessageDialog,
  ],
  providers: [
    SharedModule,
     AppComponent,NeedAuthGuard, UserNeedAuthGuard, GoogleMapsAPIWrapper,
  ],
  bootstrap: [
    AppComponent,
  ]
})
export class AppModule {}

// platformBrowserDynamic().bootstrapModule(AppModule).then(ref => {
//   // Ensure Angular destroys itself on hot reloads.
//   if (window['ngRef']) {
//     window['ngRef'].destroy();
//     console.log("destroy")
//   }
//   window['ngRef'] = ref;

//   // Otherwise, log the boot error
// }).catch(err => console.error(err));
