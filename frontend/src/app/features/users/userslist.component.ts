import { Component, OnInit } from '@angular/core';
import { UserService, User } from '../../core/services/user.service';
import { AuthService } from '../../core/auth.service';

@Component({
  templateUrl: './userslist.component.html'
})
export class UsersListComponent implements OnInit {
  users: User[] = [];
  showCreateForm = false;

  constructor(
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.userService.list().subscribe(users => {
      this.users = users;
    });
  }

  isAdmin(): boolean {
    return !!this.authService.accessToken();
  }

  toggleCreateForm() {
    this.showCreateForm = !this.showCreateForm;
  }

  onUserCreated() {
    this.showCreateForm = false;
    this.load();
  }

  onCreateCancelled() {
    this.showCreateForm = false;
  }

  deleteUser(user: User) {
    if (confirm(`Are you sure you want to delete user "${user.username}"?`)) {
      this.userService.delete(user.username).subscribe({
        next: () => {
          this.load();
        },
        error: (err) => {
          alert('Failed to delete user: ' + (err.error?.message || 'Unknown error'));
        }
      });
    }
  }
}
