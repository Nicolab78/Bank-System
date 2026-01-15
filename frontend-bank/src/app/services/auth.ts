import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User } from '../models/user.model';

interface LoginCredentials {
  username: string;
  password: string;
}

interface RegisterData {
  username: string;
  email: string;
  password: string;
}

interface AuthResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  user: User;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  isAuthenticated = signal<boolean>(false);
  currentUser = signal<User | null>(null);
  token = signal<string | null>(null);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.initializeAuth();
  }

  private initializeAuth() {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');

    if (token && user) {
      this.token.set(token);
      this.currentUser.set(JSON.parse(user));
      this.isAuthenticated.set(true);
    }
  }

  login(credentials: LoginCredentials) {
    this.loading.set(true);
    this.error.set(null);

    this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).subscribe({
      next: (response) => {
        this.setAuthData(response);
        this.loading.set(false);
        this.router.navigate(['/users']);
      },
      error: (error) => {
        this.error.set(error.error || 'Erreur de connexion');
        this.loading.set(false);
      }
    });
  }

  register(data: RegisterData) {
    this.loading.set(true);
    this.error.set(null);

    this.http.post<AuthResponse>(`${this.apiUrl}/register`, data).subscribe({
      next: (response) => {
        this.setAuthData(response);
        this.loading.set(false);
        this.router.navigate(['/users']);
      },
      error: (error) => {
        this.error.set(error.error || 'Erreur d\'inscription');
        this.loading.set(false);
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');

    this.token.set(null);
    this.currentUser.set(null);
    this.isAuthenticated.set(false);

    this.router.navigate(['/login']);
  }

  private setAuthData(response: AuthResponse) {
    localStorage.setItem('token', response.accessToken);
    localStorage.setItem('user', JSON.stringify(response.user));

    this.token.set(response.accessToken);
    this.currentUser.set(response.user);
    this.isAuthenticated.set(true);
  }

  getToken(): string | null {
    return this.token();
  }
}