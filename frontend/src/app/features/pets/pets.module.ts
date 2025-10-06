import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { PetsListComponent } from './petslist.component';

const routes: Routes = [{ path: '', component: PetsListComponent }];

@NgModule({
  declarations: [PetsListComponent],
  imports: [CommonModule, HttpClientModule, RouterModule.forChild(routes)]
})
export class PetsModule {}
