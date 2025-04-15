import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StudentService } from '../../services/student.service';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent implements OnInit {
  isLoading = true;
  isSuccess = false;
  errorMessage = '';
  userType = '';
  retryCount = 0;
  maxRetries = 3;
  token = '';
  debugInfo = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private studentService: StudentService
  ) {}

  ngOnInit(): void {
    console.log('VerifyEmailComponent initialized');
    this.route.queryParams.subscribe(params => {
      this.userType = params['userType'] || 'student';
      this.token = params['token'] || '';
      console.log('URL parameters:', { userType: this.userType, token: this.token?.substring(0, 10) + '...' });
      
      if (this.token) {
        this.verifyEmail();
      } else {
        this.isLoading = false;
        this.errorMessage = 'Verification token is missing.';
      }
    });
  }

  verifyEmail(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.debugInfo = '';
    this.retryCount++;
    
    // Логируем основную инфо для отладки
    this.debugInfo = `Verification attempt ${this.retryCount}/${this.maxRetries}\n`;
    this.debugInfo += `Time: ${new Date().toISOString()}\n`;
    this.debugInfo += `User type: ${this.userType}\n`;
    this.debugInfo += `Token (partial): ${this.token.substring(0, 10)}...\n`;
    this.debugInfo += `API URL: ${environment.apiBase}/api/students/create-student/verify\n`;
    
    // Вызываем сервис для верификации
    this.studentService.verifyEmail(this.token).subscribe({
      next: (response) => {
        console.log('Verification response:', response);
        this.isLoading = false;
        
        if (response.success) {
          this.isSuccess = true;
          this.debugInfo += 'Status: Success\n';
          this.debugInfo += `Response message: ${response.message}\n`;
          
          // Перенаправляем на страницу входа после успешной верификации
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 3000);
        } else {
          this.isSuccess = false;
          this.errorMessage = response.message || 'Verification failed. Please try again or contact support.';
          
          this.debugInfo += 'Status: Failed\n';
          this.debugInfo += `Response status: ${response.status}\n`;
          this.debugInfo += `Error message: ${response.message}\n`;
          
          if (response.connectionDetails) {
            this.debugInfo += `URL: ${response.connectionDetails.url}\n`;
            this.debugInfo += `Status text: ${response.connectionDetails.statusText}\n`;
          }
        }
      },
      error: (error) => {
        console.error('Error during verification:', error);
        this.isLoading = false;
        this.isSuccess = false;
        this.errorMessage = 'An unexpected error occurred. Please try again.';
        this.debugInfo += 'Status: Error\n';
        this.debugInfo += `Error: ${JSON.stringify(error)}\n`;
      }
    });
  }

  // Метод для ручного повтора верификации
  retryVerification(): void {
    if (this.retryCount < this.maxRetries) {
      this.verifyEmail();
    } else {
      this.errorMessage = `Maximum verification attempts reached. Please request a new verification email.`;
      this.debugInfo += '\nMaximum retries reached';
    }
  }

  // Перейти на страницу входа
  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}
