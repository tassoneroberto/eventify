import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SigninPageComponent } from './signin-page.component';

describe('LoginPageComponent', () => {
  let component: SigninPageComponent;
  let fixture: ComponentFixture<SigninPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SigninPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SigninPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
