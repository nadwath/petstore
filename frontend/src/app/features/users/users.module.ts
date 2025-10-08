import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { UsersListComponent } from './userslist.component';
import { CreateUserComponent } from './create-user.component';

const routes: Routes = [{ path: '', component: UsersListComponent }];

@NgModule({
  declarations: [UsersListComponent, CreateUserComponent],
  imports: [CommonModule, HttpClientModule, FormsModule, RouterModule.forChild(routes)]
})
export class UsersModule {}
