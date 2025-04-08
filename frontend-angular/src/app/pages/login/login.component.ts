import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AuthRequest } from '../../models/auth-request.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  authRequest: AuthRequest = {
    email: '',
    password: ''
  };
  
  userType: 'student' | 'mentor' = 'student'; // По умолчанию - студент
  errorMessage: string | null = null;
  isLoading = false;
  currentYear = new Date().getFullYear();

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.userType, this.authRequest).subscribe({
      next: (user) => {
        this.isLoading = false;
        // После успешного входа перенаправляем на нужную страницу
        if (this.userType === 'mentor') {
          this.router.navigate(['/cabinet-mentor']);
        } else {
          this.router.navigate(['/cabinet-student']);
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Произошла ошибка при входе. Пожалуйста, попробуйте снова.';
      }
    });
  }

  loginWithGoogle(): void {
    // Здесь будет интеграция с Google OAuth, для примера просто показываем логику
    // В реальном приложении используйте Google SDK или OAuth 2.0
    const fakeGoogleToken = 'fake-google-token';
    
    this.authService.loginWithGoogle(this.userType, fakeGoogleToken).subscribe({
      next: (user) => {
        if (this.userType === 'mentor') {
          this.router.navigate(['/cabinet-mentor']);
        } else {
          this.router.navigate(['/cabinet-student']);
        }
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Произошла ошибка при входе через Google.';
      }
    });
  }

  loginWithLinkedIn(): void {
    // Здесь будет интеграция с LinkedIn OAuth
    const fakeLinkedInToken = 'fake-linkedin-token';
    
    this.authService.loginWithLinkedIn(this.userType, fakeLinkedInToken).subscribe({
      next: (user) => {
        if (this.userType === 'mentor') {
          this.router.navigate(['/cabinet-mentor']);
        } else {
          this.router.navigate(['/cabinet-student']);
        }
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Произошла ошибка при входе через LinkedIn.';
      }
    });
  }

  goToRegistration(): void {
    this.router.navigate(['/registration-selector']);
  }
}
