import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Category } from '../shared/category.model'
import { FormControl } from '@angular/forms';

@Component({
  selector: 'filters',
  templateUrl: './filters.component.html',
  styleUrls: ['./filters.component.scss']
})
export class FiltersComponent implements OnInit {
  selectedCategories:string[];



  @Input()
  categories: string[]


  @Input()
  priceFilters: any[]

  @Output()
  onFilterChange = new EventEmitter<any>()

  toppings = new FormControl();
  priceChecked: string='All';


  sideShown: boolean = false

  constructor() {
    

   }

  ngOnInit() {
    
    this.selectedCategories=this.categories;
    
  }

  reset( priceFilters){
    this.priceFilters = priceFilters
   
  }

  onInputChange($event, filter, type){
    
    let change=0
    let isChecked
    if(type=="category"){
      change = $event.source._selected ? 1: -1
      isChecked=$event.source._selected
    }
    else{
    change = $event.target.checked ? 1: -1
    isChecked=$event.target.checked
    }

    this.onFilterChange.emit({
      type: type,
      filter: filter,
      isChecked: isChecked,
      change: change
    })
  }
}
