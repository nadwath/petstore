import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { PetService, Pet } from '../../core/services/pet.service';
import { CategoryService, Category } from '../../core/services/category.service';
import { TagService, Tag } from '../../core/services/tag.service';

@Component({
  selector: 'app-create-pet',
  templateUrl: './create-pet.component.html'
})
export class CreatePetComponent implements OnInit {
  @Output() petCreated = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  categories: Category[] = [];
  tags: Tag[] = [];
  selectedTagIds: number[] = [];

  newPet: any = {
    name: '',
    status: 'AVAILABLE',
    categoryId: null,
    photoUrls: [],
    tagIds: []
  };

  constructor(
    private petService: PetService,
    private categoryService: CategoryService,
    private tagService: TagService
  ) {}

  ngOnInit() {
    this.loadCategories();
    this.loadTags();
  }

  loadCategories() {
    this.categoryService.list().subscribe(categories => {
      this.categories = categories;
    });
  }

  loadTags() {
    this.tagService.list().subscribe(tags => {
      this.tags = tags;
    });
  }

  onTagChange(tagId: number, event: any) {
    if (event.target.checked) {
      this.selectedTagIds.push(tagId);
    } else {
      const index = this.selectedTagIds.indexOf(tagId);
      if (index > -1) {
        this.selectedTagIds.splice(index, 1);
      }
    }
  }

  createPet() {
    if (!this.newPet.name.trim()) {
      alert('Pet name is required');
      return;
    }

    const petData: any = {
      name: this.newPet.name,
      status: this.newPet.status,
      photoUrls: this.newPet.photoUrls || []
    };

    if (this.newPet.categoryId) {
      petData.category = { id: this.newPet.categoryId };
    }

    if (this.selectedTagIds.length > 0) {
      petData.tags = this.selectedTagIds.map(id => ({ id }));
    }

    this.petService.create(petData).subscribe({
      next: () => {
        this.resetForm();
        this.petCreated.emit();
      },
      error: (err) => {
        alert('Failed to create pet: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }

  cancel() {
    this.resetForm();
    this.cancelled.emit();
  }

  resetForm() {
    this.newPet = {
      name: '',
      status: 'AVAILABLE',
      categoryId: null,
      photoUrls: [],
      tagIds: []
    };
    this.selectedTagIds = [];
  }
}
