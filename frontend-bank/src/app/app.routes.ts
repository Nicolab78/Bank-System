import { Routes } from '@angular/router';
import { UserList } from './components/users/user-list/user-list';
import { authGuard } from './guards/auth-guard';
import { Login } from './components/auth/login/login';
import { Register } from './components/auth/register/register';

export const routes: Routes = [
    { path: 'login', component: Login },
        { path: 'register', component: Register },
    { 
        path: 'users', 
        component: UserList,
        canActivate: [authGuard]
    },
    { path: '', redirectTo: '/users', pathMatch: 'full' },
    { path: '**', redirectTo: '/login' }
];