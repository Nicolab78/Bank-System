import { Routes } from '@angular/router';
import { UserList } from './components/users/user-list/user-list';

export const routes: Routes = [
    { path: '', redirectTo: '/users', pathMatch: 'full' },
    { path: 'users', component: UserList },
    { path: '**', redirectTo: '/users' }

];
