import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PetService, Pet } from '../../core/services/pet.service';
import { CategoryService, Category } from '../../core/services/category.service';
import { TagService, Tag } from '../../core/services/tag.service';

@Component({
  selector: 'app-edit-pet',
  templateUrl: './edit-pet.component.html'
})
export class EditPetComponent implements OnInit {
  @Input() pet!: Pet;
  @Output() petUpdated = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  categories: Category[] = [];
  tags: Tag[] = [];
  selectedTagIds: number[] = [];

  editPet: any = {
    name: '',
    status: 'AVAILABLE',
    categoryId: null
  };

  constructor(
    private petService: PetService,
    private categoryService: CategoryService,
    private tagService: TagService
  ) {}

  ngOnInit() {
    this.loadCategories();
    this.loadTags();
    this.initializeForm();
  }

  initializeForm() {
    this.editPet = {
      name: this.pet.name,
      status: this.pet.status,
      categoryId: this.pet.category?.id || null
    };
    this.selectedTagIds = this.pet.tags?.map(t => t.id) || [];
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

  isTagSelected(tagId: number): boolean {
    return this.selectedTagIds.includes(tagId);
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

  updatePet() {
    if (!this.editPet.name.trim()) {
      alert('Pet name is required');
      return;
    }

    const petData: any = {
      name: this.editPet.name,
      status: this.editPet.status
    };

    if (this.editPet.categoryId) {
      petData.category = { id: this.editPet.categoryId };
    }

    if (this.selectedTagIds.length > 0) {
      petData.tags = this.selectedTagIds.map(id => ({ id }));
    }

    this.petService.update(this.pet.id!, petData).subscribe({
      next: () => {
        this.petUpdated.emit();
      },
      error: (err) => {
        alert('Failed to update pet: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }

  cancel() {
    this.cancelled.emit();
  }
}
