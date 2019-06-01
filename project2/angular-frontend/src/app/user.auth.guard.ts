import {
  CanActivate,
  Router
} from '@angular/router';
import {
  Injectable
} from '@angular/core';
import {
  CustomerService
} from './customer.service';
import {
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router/src/router_state';

@Injectable()
export class UserNeedAuthGuard implements CanActivate {

  constructor(private customerService: CustomerService, private router: Router) {}
  logout() {

    this.customerService.removeToken();
    this.router.navigateByUrl('/login');


    return true;
  }


  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    const redirectUrl = route['_routerState']['url'];

    if (this.customerService.isLogged()&&this.customerService.isUser()) {
      return true;
    }

    this.router.navigateByUrl(
      this.router.createUrlTree(
        ['/login'], {
          queryParams: {
            redirectUrl
          }
        }
      )
    );

    return false;
  }
}
