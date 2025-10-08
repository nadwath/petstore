import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { PetsListComponent } from './petslist.component';
import { CreatePetComponent } from './create-pet.component';
import { EditPetComponent } from './edit-pet.component';

const routes: Routes = [{ path: '', component: PetsListComponent }];

@NgModule({
  declarations: [PetsListComponent, CreatePetComponent, EditPetComponent],
  imports: [CommonModule, HttpClientModule, FormsModule, RouterModule.forChild(routes)]
})
export class PetsModule {}
