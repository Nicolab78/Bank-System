import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../../services/user';
import { CreateUser } from '../../../models/user.model';

@Component({
  selector: 'app-user-form',
  imports: [ReactiveFormsModule],
  templateUrl: './user-form.html',
  styleUrl: './user-form.css',
})
export class UserForm {

  userForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService
  ) {
    this.userForm = this.fb.group({
      username: ['',[Validators.required, Validators.minLength(3)]],
      email: ['',[Validators.required, Validators.email]],
      password: ['',[Validators.required, Validators.minLength(8)]],
      role: ['USER', [Validators.required]]
    })
  }

  onSubmit(){
    if(this.userForm.valid){
      const userData: CreateUser = this.userForm.value;
      console.log('Données du formulaire:', userData);
      this.userService.createUser(userData);
      this.userForm.reset();
    }
  }

  get username() { return this.userForm.get('username')}
  get email() { return this.userForm.get('email')}
  get password() { return this.userForm.get('password')}
  get role() { return this.userForm.get('role'); }
}
