import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { BlogComponent } from './pages/blog/blog.component';
import { LoginComponent } from './pages/login/login.component';
import { DonateComponent } from './pages/donate/donate.component';
import { MentorRegistrationComponent } from './pages/mentor-registration/mentor-registration.component';
import { RegistrationSelectorComponent } from './pages/registration-selector/registration-selector.component';
import { CabinetStudentComponent } from './pages/cabinet-student/cabinet-student.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'blog', component: BlogComponent },
  { path: 'login', component: LoginComponent },
  { path: 'donate', component: DonateComponent },
  { path: 'mentor-registration', component: MentorRegistrationComponent },
  { path: 'registration-selector', component: RegistrationSelectorComponent },
  { path: 'cabinet-student', component: CabinetStudentComponent },
  { path: '**', component: NotFoundComponent }
];
