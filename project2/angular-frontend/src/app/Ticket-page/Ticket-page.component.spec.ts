/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { TicketPageComponent } from './Ticket-page.component';

describe('TicketPageComponent', () => {
  let component: TicketPageComponent;
  let fixture: ComponentFixture<TicketPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TicketPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
