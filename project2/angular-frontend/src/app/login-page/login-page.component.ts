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
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {

  email = '';
  password = '';

  hide = true;
  form_email = new FormControl('', [Validators.required, Validators.email]);

  constructor(public snackBar: MatSnackBar,private api: ApiService, private customer: CustomerService, private router: Router) {}

  tryLogin() {
    this.api.loginUser(
        this.email,
        this.password
      )
      .subscribe(
        r => {
          if (r.token) {
            this.customer.setToken(r.token);
            this.customer.setName(r.name);
            this.customer.setSurname(r.surname);
            this.customer.setId(r.loginId.toString());
            this.customer.setEmail(r.email);
            this.customer.setIsUser();
            this.router.navigateByUrl('/store');
          } else {
            this.snackBar.open(r.message, "", {
              duration: 1000
            });
          }
        },
        r => {
          this.snackBar.open(r.message, "", {
            duration: 1000
          });        });
  }

  tryLoginOrganizer() {
    this.api.loginOrganizer(
        this.email,
        this.password
      )
      .subscribe(
        r => {
          if (r.token) {
            this.customer.setToken(r.token);
            this.customer.setEmail(r.email);
            this.customer.setPhone(r.phone);
            this.customer.setId(r.loginId.toString());
            this.customer.setUser(r.username);
            this.customer.setIsOrganizer();
            this.router.navigateByUrl('/dashboard');

          } else {
            this.snackBar.open(r.message, "", {
              duration: 1000
            });
          }
        },
        r => {
          this.snackBar.open(r.message, "", {
            duration: 1000
          });        });
  }

}
