import { Component, OnInit } from '@angular/core';
import { PetService, Pet } from '../../core/services/pet.service';
import { AuthService } from '../../core/auth.service';

@Component({
  templateUrl: './petslist.component.html'
})
export class PetsListComponent implements OnInit {
  pets: Pet[] = [];
  searchText: string = '';
  selectedStatus: string = 'all';
  statuses = ['all', 'AVAILABLE', 'PENDING', 'SOLD'];
  showCreateForm = false;
  editingPet: Pet | null = null;

  constructor(
    private petService: PetService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    if (this.selectedStatus === 'all') {
      this.petService.list().subscribe(pets => {
        this.pets = pets;
        this.applySearch();
      });
    } else {
      this.petService.findByStatus(this.selectedStatus).subscribe(pets => {
        this.pets = pets;
        this.applySearch();
      });
    }
  }

  applySearch() {
    if (this.searchText.trim()) {
      const search = this.searchText.toLowerCase();
      this.pets = this.pets.filter(p =>
        p.name.toLowerCase().includes(search) ||
        p.category?.name?.toLowerCase().includes(search) ||
        p.tags?.some(t => t.name.toLowerCase().includes(search))
      );
    }
  }

  onSearchChange() {
    this.load();
  }

  onStatusChange() {
    this.load();
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  toggleCreateForm() {
    this.showCreateForm = !this.showCreateForm;
    this.editingPet = null;
  }

  onPetCreated() {
    this.showCreateForm = false;
    this.load();
  }

  onCreateCancelled() {
    this.showCreateForm = false;
  }

  editPet(pet: Pet) {
    this.editingPet = pet;
    this.showCreateForm = false;
  }

  onPetUpdated() {
    this.editingPet = null;
    this.load();
  }

  onEditCancelled() {
    this.editingPet = null;
  }

  deletePet(pet: Pet) {
    if (confirm(`Are you sure you want to delete "${pet.name}"?`)) {
      this.petService.delete(pet.id!).subscribe({
        next: () => {
          this.load();
        },
        error: (err) => {
          alert('Failed to delete pet: ' + (err.error?.message || 'Unknown error'));
        }
      });
    }
  }
}
