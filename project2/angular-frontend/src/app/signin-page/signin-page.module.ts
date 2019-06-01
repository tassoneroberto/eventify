import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SigninPageComponent } from './signin-page.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  imports: [
    CommonModule,    FormsModule

  ],
  declarations: [SigninPageComponent]
})
export class SigninPageModule { }


