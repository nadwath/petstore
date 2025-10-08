import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../core/auth.service';

@Component({
    templateUrl: './login.component.html'
})
export class LoginComponent {
    u = '';
    p = '';
    errorMessage = '';
    isLoading = false;

    constructor(
        private auth: AuthService,
        private router: Router
    ) {
    }

    async login() {
        if (!this.u || !this.p) {
            this.errorMessage = 'Please enter username and password';
            return;
        }

        try {
            this.isLoading = true;
            this.errorMessage = '';

            const pub = await this.auth.getPublicKey().toPromise();

            // Extract the key from response (handle both data wrapper and direct response)
            const publicKeyString = pub.data?.key || pub.key;

            if (!publicKeyString) {
                throw new Error('Public key not found in response');
            }

            // Clean the base64 string (remove whitespace and newlines)
            const cleanedKey = publicKeyString.replace(/\s/g, '');

            const keyData = Uint8Array.from(atob(cleanedKey), c => c.charCodeAt(0));
            const key = await window.crypto.subtle.importKey('spki', keyData, {
                name: 'RSA-OAEP',
                hash: 'SHA-256'
            }, false, ['encrypt']);
            const enc = new TextEncoder().encode(this.p);
            const cip = await window.crypto.subtle.encrypt({
                name: 'RSA-OAEP'
            }, key, enc);
            const b64 = btoa(String.fromCharCode(...new Uint8Array(cip)));

            this.auth.login(this.u, b64).subscribe({
                next: () => {
                    this.router.navigate(['/pets']);
                },
                error: (err) => {
                    this.errorMessage = err.error?.message || 'Login failed. Please try again.';
                    this.isLoading = false;
                }
            });
        } catch (error: any) {
            console.error('Login error:', error);
            this.errorMessage = error.message || 'An error occurred during login';
            this.isLoading = false;
        }
    }
}
