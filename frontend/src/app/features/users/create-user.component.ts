import { Component, EventEmitter, Output } from '@angular/core';
import { UserService, User } from '../../core/services/user.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html'
})
export class CreateUserComponent {
  @Output() userCreated = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  roles = ['ADMIN', 'USER'];
  selectedRoles: string[] = [];

  newUser: User = {
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: '',
    phone: '',
    userStatus: 1,
    roleIds: []
  };

  constructor(
    private userService: UserService
  ) {}

  onRoleChange(role: string, event: any) {
    if (event.target.checked) {
      this.selectedRoles.push(role);
    } else {
      const index = this.selectedRoles.indexOf(role);
      if (index > -1) {
        this.selectedRoles.splice(index, 1);
      }
    }
  }

  createUser() {
    if (!this.newUser.username.trim()) {
      alert('Username is required');
      return;
    }
    if (!this.newUser.password || this.newUser.password.length < 4) {
      alert('Password must be at least 4 characters');
      return;
    }
    if (this.selectedRoles.length === 0) {
      alert('Please select at least one role');
      return;
    }

    this.newUser.roleIds = this.selectedRoles as any;

    this.userService.create(this.newUser).subscribe({
      next: () => {
        this.resetForm();
        this.userCreated.emit();
      },
      error: (err) => {
        alert('Failed to create user: ' + (err.error?.message || 'Unknown error'));
      }
    });
  }

  cancel() {
    this.resetForm();
    this.cancelled.emit();
  }

  resetForm() {
    this.newUser = {
      username: '',
      password: '',
      email: '',
      firstName: '',
      lastName: '',
      phone: '',
      userStatus: 1,
      roleIds: []
    };
    this.selectedRoles = [];
  }
}
