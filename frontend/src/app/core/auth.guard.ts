import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthService} from './auth.service';

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(private auth: AuthService, private router: Router) {
    }

    canActivate(): boolean {
        if (this.auth.accessToken() || this.auth.refreshToken()) return true;
        this.router.navigate(['/auth/login']);
        return false;
    }
}
