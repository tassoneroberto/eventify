import { CartService } from './../cart.service';
import { ApiService } from './../api.service';
import { NgModule } from '@angular/core';
import { CustomerService } from '../customer.service';
import { ModuleWithProviders } from '@angular/compiler/src/core';
@NgModule({
  providers: [ApiService,CustomerService,CartService]
})
export class SharedModule {
  public title = 'Eventify';
  categories: string[] = ['Art','Circus','Disco','Festival','Food','Live','Museum','Science','Sport','Theatre',
    'Outdoor','Cinema'
  ];
}

