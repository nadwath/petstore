import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {APP_ROUTES} from './app.routes';
import {AuthInterceptor} from './core/auth.interceptor';
import {AuthGuard} from './core/auth.guard';

@NgModule({
    declarations: [AppComponent],
    imports: [BrowserModule, HttpClientModule, FormsModule, RouterModule.forRoot(APP_ROUTES)],
    providers: [
        {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
        AuthGuard
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
