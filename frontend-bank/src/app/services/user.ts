import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User, CreateUser, UpdateUser } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = "http://localhost:8080/api/users"

  users = signal<User[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  constructor(private http: HttpClient){}

  fetchUser(){
    this.loading.set(true)
    this.error.set(null);

    this.http.get<User[]>(`${this.apiUrl}/all`).subscribe({
      next: data => {
        this.users.set(data);
        this.loading.set(false);
      },
      error: err => {
        this.error.set(err.error || 'Erreur lors du chargement');
        this.loading.set(false);
      }
    });

  }

  createUser(user: CreateUser){
    this.http.post<User>(`${this.apiUrl}/create`, user).subscribe({
      next: newUser => {
        this.users.update(arr => [...arr, newUser]);
      },
      error: err => this.error.set(err.error || 'Erreur lors de la création')
    });
  }

  update(id: number, user: UpdateUser){
    this.http.put<User>(`${this.apiUrl}/update/${id}`, user).subscribe({
      next: updateUser => {
        this.users.update(arr =>
          arr.map(u => u.id === id ? updateUser : u)
        );
      },
      error: err => this.error.set(err.error || 'Erreur lors de la modification')
    });
  }

  delete(id: number){
    this.http.delete(`${this.apiUrl}/delete/${id}`).subscribe({
      next: () => {
        this.users.update(arr => arr.filter(u => u.id != id));
      },
      error: err => this.error.set(err.error || 'Erreur lors de la suppression')
    });
  }

  
}
