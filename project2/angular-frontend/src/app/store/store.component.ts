import {
  Event
} from './../event';

import {
  AfterViewInit,
  ViewChild
} from '@angular/core';

import {
  FiltersComponent
} from '../filters/filters.component';
import {
  SearchBarComponent
} from '../search-bar/search-bar.component';
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

import { EventAmount } from '../model/EventAmount';
import { Route } from '@angular/compiler/src/core';
import { Router } from '@angular/router';
import { SharedModule } from '../shared/shared.module';

@Component({
  selector: 'app-root',
  templateUrl: './store.component.html',
  styleUrls: ['./store.component.css']
})

export class StoreComponent implements OnInit, OnDestroy {
  loadEvents(): any {
    this.api.getCustomEvents().subscribe(r => {
      this.events = r;
      this.originalevents = r;
      console.log("recived events!",r);
    });
  }

tickets:EventAmount[];
  events: Event[];
  originalevents: Event[];

  mainFilter: any

  currentSorting: string

  @ViewChild('filtersComponent')
  filtersComponent: FiltersComponent;

  @ViewChild('searchComponent')
  searchComponent: SearchBarComponent;

  sortFilters: any[] = [{
      name: 'Name (A to Z)',
      value: 'name'
    },
    {
      name: 'Price (low to high)',
      value: 'priceAsc'
    },
    {
      name: 'Price (high to low)',
      value: 'priceDes'
    }
  ]

 
  //   {
  //     name: 'Not Available',
  //     value: 'unavailable',
  //     checked: false
  //   },
  //   {
  //     name: 'Bestseller',
  //     value: 'bestseller',
  //     checked: false
  //   }

  priceFilters: any[] = [{
      name: 'All',
      value: 'all',
      checked: true
    },
    {
      name: 'Price > 50.00',
      value: 'more_50',
      checked: false
    },
    {
      name: 'Price < 30.00',
      value: 'less_30',
      checked: false
    }
  ]



  constructor(private router: Router, private api: ApiService, private app: SharedModule, private c: CustomerService, public dialog: MatDialog, changeDetectorRef: ChangeDetectorRef, media: MediaMatcher) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
    // this.getEvents();
    this.loadEvents();


  }
  onSelectChange($event){
    this.loadEvents();
   
  }
  ngOnInit() {
    this.mainFilter = {
      search: '',
      categories: this.app.categories,
      priceFilter: this.priceFilters[0]
    }
    // this.api.getTickets().subscribe(r => {
    //   this.tickets = r;
    // });
  

  }





  onSearchChange(search) {
    this.mainFilter.search = search.search
    this.updateevents({
      type: 'search',
      change: search.change
    })
  }

  onFilterChange(data) {
    if (data.type == 'category') {
      if (data.isChecked) {
        this.mainFilter.categories.push(data.filter)
      } else {
        this.mainFilter.categories = this.mainFilter.categories.filter(category => {
          return category != data.filter
        })
      }
    } else if (data.type == 'custom') {
      this.mainFilter.customFilter = data.filter
    } else if (data.type == 'price') {
      this.mainFilter.priceFilter = data.filter
    }
    this.updateevents({
      type: data.type,
      change: data.change
    })
  }

  updateevents(filter) {
    if(this.events==undefined)return;
    let eventsSource = this.originalevents;
    let prevevents = this.events
    let filterAllData = true
    if ((filter.type == 'search' && filter.change == 1) || (filter.type == 'category' && filter.change == -1)) {
      eventsSource = this.events
      filterAllData = false
    }
    //console.log('filtering ' + eventsSource.length + ' events')

    this.events = eventsSource.filter(event => {
      //Filter by search
      if (filterAllData || filter.type == 'search') {
        if (!event.title.match(new RegExp(this.mainFilter.search, 'i'))) {
          return false;
        }
      }

      //Filter by categories
      if (filterAllData || filter.type == 'category') {
        let passCategoryFilter = false

        if (!passCategoryFilter) {
          passCategoryFilter = this.mainFilter.categories.reduce((found, category) => {
            return found || event.category == category
          }, false)
        }

        if (!passCategoryFilter) {
          return false
        }
      }

      // //Filter by custom filters
      // if(filterAllData || filter.type=='custom'){
      //   let passCustomFilter = false
      //   let customFilter = this.mainFilter.customFilter.value
      //   if(customFilter == 'all'){
      //     passCustomFilter = true;
      //   }else if(customFilter == 'available' && event.available){
      //     passCustomFilter = true;
      //   }else if(customFilter == 'unavailable' && !event.available){
      //     passCustomFilter = true;
      //   }else if(customFilter == 'bestseller' && event.best_seller){
      //     passCustomFilter = true;
      //   }
      //   if(!passCustomFilter){
      //     return false
      //   }
      // }

      //Filter by price filters
      if (filterAllData || filter.type == 'price') {
        let passPriceFilter = false
        let customFilter = this.mainFilter.priceFilter.value
        let eventPrice = event.price;
        if (customFilter == 'all') {
          passPriceFilter = true;
        } else if (customFilter == 'more_50' && eventPrice > 50) {
          passPriceFilter = true;
        } else if (customFilter == 'less_30' && eventPrice < 30) {
          passPriceFilter = true;
        }
        if (!passPriceFilter) {
          return false
        }
      }

      return true
    })

    //If the number of events increased after the filter has been applied then sort again
    //If the number of events remained equal, there's a high chance that the items have been reordered.
    if (prevevents.length <= this.events.length && this.events.length > 1) {
      this.sortevents(this.currentSorting)
    }

    //These two types of filters usually add new data to the events showcase so a sort is necessary
    if (filter.type == 'custom' || filter.type == 'price') {
      this.sortevents(this.currentSorting)
    }
  }

  sortevents(criteria) {
    //console.log('sorting ' + this.events.length + ' events')
    this.events.sort((a, b) => {
      let priceComparison = a.price - b.price;
      //  parseFloat(a.price.toString().replace(/\./g, '').replace(',', '.')) - parseFloat(b.price.toString().replace(/\./g, '').replace(',', '.'))

      if (criteria == 'priceDes') {
        return -priceComparison
      } else if (criteria == 'priceAsc') {
        return priceComparison
      } else if (criteria == 'name') {
        let nameA = a.title.toLowerCase(),
          nameB = b.title.toLowerCase()
        if (nameA < nameB)
          return -1;
        if (nameA > nameB)
          return 1;
        return 0;
      } else {
        console.log("unexpected sort criteria");
        //Keep the same order in case of any unexpected sort criteria
        return -1
      }
    })
    this.currentSorting = criteria
  }



  mobileQuery: MediaQueryList;


  private _mobileQueryListener: () => void;
  title = this.app.title;
  customer = this.c;
  message: string;
 
  logout(): void {
    this.api.logoutOrganizer();
  }



  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }


  goToTickets = function () {
    this.router.navigateByUrl('/tickets');
  };

}
