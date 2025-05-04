import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { BlogComponent } from './pages/blog/blog.component';
import { LoginComponent } from './pages/login/login.component';
import { DonateComponent } from './pages/donate/donate.component';
import { CreateMentorComponent } from './pages/create-mentor/create-mentor.component';
import { RegistrationSelectorComponent } from './pages/registration-selector/registration-selector.component';
import { CreateStudentComponent } from './pages/create-student/create-student.component';
import { CabinetStudentComponent } from './pages/cabinet-student/cabinet-student.component';
import { CabinetMentorComponent } from './pages/cabinet-mentor/cabinet-mentor.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { RegistrationSuccessComponent } from './pages/registration-success/registration-success.component';
import { VerifyEmailComponent } from './pages/verify-email/verify-email.component';
import { authGuard } from './core/guards/auth.guard';
import { ProfileComponent } from './pages/profile/profile.component';
import { ProfileEditComponent } from './pages/profile-edit/profile-edit.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'blog', component: BlogComponent },
  { path: 'login', component: LoginComponent },
  { path: 'donate', component: DonateComponent },
  { path: 'create-mentor', component: CreateMentorComponent },
  { path: 'registration-selector', component: RegistrationSelectorComponent },
  { path: 'create-student', component: CreateStudentComponent },
  { path: 'registration-success', component: RegistrationSuccessComponent },
  { path: 'verify-email', component: VerifyEmailComponent },
  { 
    path: 'cabinet-student', 
    component: CabinetStudentComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'cabinet-mentor', 
    component: CabinetMentorComponent,
    canActivate: [authGuard]
  },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'profile/edit', component: ProfileEditComponent, canActivate: [authGuard] },
  { path: '**', component: NotFoundComponent }
];
