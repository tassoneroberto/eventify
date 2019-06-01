import {
  Component
} from '@angular/core';
import {
  ApiService
} from '../api.service';
import {
  CustomerService
} from '../customer.service';
import {
  Router
} from '@angular/router';
import {
  FormControl,
  Validators
} from '@angular/forms';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-signin-page',
  templateUrl: './signin-page.component.html',
  styleUrls: ['./signin-page.component.css']
})
export class SigninPageComponent {




  email = '';
  password = '';
  name = '';
  username = "";
  surname = '';
  phone = ""

  hide = true;
  form_email = new FormControl('', [Validators.required, Validators.email]);

  constructor(private snackBar:MatSnackBar,private api: ApiService, private customer: CustomerService, private router: Router) {}

  trySignin() {
    this.api.signinUser(
        this.email,
        this.password,
        this.name,
        this.surname
      )
      .subscribe(
        r => {
          if (r.token) {

            this.customer.setToken(r.token);
            this.customer.setName(r.name);
            this.customer.setSurname(r.surname);
            this.customer.isUser();

            this.customer.setId(r.loginId.toString());
            this.customer.setEmail(r.email);
            this.router.navigateByUrl('/store');
          } else {
            this.snackBar.open(r.message, "", {
              duration: 1000
            });          }
        });

  }

  trySigninOrganizer() {
    this.api.signinOrganizer(
        this.email,
        this.password,
        this.username,
        this.phone
      )
      .subscribe(
        r => {

          if (r.token) {
            this.customer.setToken(r.token);
            this.customer.setEmail(r.email);
            this.customer.setPhone(r.phone);
            this.customer.setId(r.loginId.toString());
            this.customer.isOrganizer();
            this.customer.setUser(r.username);
            this.router.navigateByUrl('/dashboard');
          } else {
            this.snackBar.open(r.message, "", {
              duration: 1000
            });          }
        }
        );

  }

}
