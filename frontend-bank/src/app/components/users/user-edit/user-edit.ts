import { Component, Input, Output, EventEmitter, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../../services/user';
import { User, UpdateUser } from '../../../models/user.model';

@Component({
  selector: 'app-user-edit',
  imports: [ReactiveFormsModule],
  templateUrl: './user-edit.html',
  styleUrl: './user-edit.css',
})
export class UserEdit implements OnInit{

  @Input() user!: User;
  @Output() editClosed = new EventEmitter<void>();

  editForm: FormGroup
  constructor(
    private fb: FormBuilder,
    private userService: UserService
  ) {
    this.editForm = this.fb.group({
      username: ['',[Validators.required, Validators.minLength(3)]],
      email: ['',[Validators.required, Validators.email]],
      password: ['', [Validators.minLength(8)]],
      role: ['', [Validators.required]] 
    })
  }

  ngOnInit() {
    this.editForm.patchValue({
      username: this.user.username,
      email: this.user.email,
      password: '',
      role: this.user.role
    });
  }

  onSubmit() {
    if (this.editForm.valid) {
      const updateData: UpdateUser = this.editForm.value;
      this.userService.update(this.user.id, updateData);
      this.editClosed.emit();
    }
  }

  onCancel() {
    this.editClosed.emit();
  }

  get username() { return this.editForm.get('username'); }
  get email() { return this.editForm.get('email'); }
  get password() { return this.editForm.get('password'); }
  get role() { return this.editForm.get('role'); }


}
