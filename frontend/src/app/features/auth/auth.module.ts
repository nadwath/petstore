import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {LoginComponent} from './login.component';

const routes: Routes = [{path: 'login', component: LoginComponent}];

@NgModule({
    declarations: [LoginComponent],
    imports: [CommonModule, FormsModule, RouterModule.forChild(routes)]
})
export class AuthModule {
}
