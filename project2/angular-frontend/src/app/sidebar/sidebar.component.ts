import { SharedModule } from './../shared/shared.module';
import { CustomerService } from './../customer.service';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  customer: CustomerService;


categories=this.app.categories;

  constructor(private c:CustomerService,public app:SharedModule) {
    this.customer=this.c;
   }

  ngOnInit() {
  }
  


}
