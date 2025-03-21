import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'registration-selector',
    loadComponent: () => import('./pages/registration-selector/registration-selector.component')
      .then(m => m.RegistrationSelectorComponent)
  },
  {
    path: 'register-student',
    loadComponent: () => import('./pages/register-student/register-student.component')
      .then(m => m.RegisterStudentComponent)
  },
  {
    path: 'register-mentor',
    loadComponent: () => import('./pages/register-mentor/register-mentor.component')
      .then(m => m.RegisterMentorComponent)
  },
  {
    path: 'cabinet-student',
    loadComponent: () => import('./pages/cabinet-student/cabinet-student.component')
      .then(m => m.CabinetStudentComponent),
    canActivate: [authGuard, roleGuard],
    data: { role: 'student' }
  },
  {
    path: 'cabinet-mentor',
    loadComponent: () => import('./pages/cabinet-mentor/cabinet-mentor.component')
      .then(m => m.CabinetMentorComponent),
    canActivate: [authGuard, roleGuard],
    data: { role: 'mentor' }
  },
  {
    path: 'mentor-list',
    loadComponent: () => import('./pages/mentor-list/mentor-list.component')
      .then(m => m.MentorListComponent)
  },
  {
    path: 'mentor/:id',
    loadComponent: () => import('./pages/mentor-details/mentor-details.component')
      .then(m => m.MentorDetailsComponent)
  },
  {
    path: '**',
    loadComponent: () => import('./pages/not-found/not-found.component')
      .then(m => m.NotFoundComponent)
  }
]; 