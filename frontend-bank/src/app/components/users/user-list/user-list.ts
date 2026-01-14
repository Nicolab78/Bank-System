import { Component, OnInit, signal } from '@angular/core';
import { UserService } from '../../../services/user';
import { User } from '../../../models/user.model';
import { UserForm } from '../user-form/user-form';
import { UserEdit } from '../user-edit/user-edit';

@Component({
  selector: 'app-user-list',
  imports: [UserForm, UserEdit],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit{

  constructor(public userService: UserService){}

  editingUser = signal<User | null>(null);

  ngOnInit(): void {
    this.userService.fetchUser();
  }

  onDeleteUser(id: number): void{
    if (confirm('Etes vous sûr de vouloir supprimer cet utilisateur ?')) {
      this.userService.delete(id);
    }
  }

  onEditUser(user: User): void{

    this.editingUser.set(user); 
    console.log('User edit :' , user);
  }

  onCreateUser(): void{
    
    console.log('Create User')
  }

}
