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
    // Start loading and reset state
    this.isLoading   = true;
    this.isSuccess   = false;
    this.errorMessage= '';
    this.retryCount++;
  
    // Build debug info header
    this.debugInfo = [
      `Verification attempt ${this.retryCount}/${this.maxRetries}`,
      `Timestamp: ${new Date().toISOString()}`,
      `User type: ${this.userType}`,
      `Token (partial): ${this.token.substring(0, 10)}...`,
      `API endpoint: ${environment.apiUrl}/students/verify`
    ].join('\n') + '\n';
  
    // Call service and handle errors in the pipe
    this.studentService.verifyEmail(this.token)
      .pipe(
        catchError(err => {
          this.handleError(err);
          // emit null to terminate stream gracefully
          return of(null);
        })
      )
      .subscribe(response => {
        if (!response) {
          // error case already handled in catchError
          return;
        }
  
        // Stop loading spinner
        this.isLoading = false;
  
        // Determine success from response status
        this.isSuccess = response.status === 'success';
  
        if (this.isSuccess) {
          // Successful verification
          this.debugInfo += `Status: Success\nMessage: ${response.message}\n`;
          // Redirect to login after a short delay
          setTimeout(() => this.router.navigate(['/login']), 3000);
        } else {
          // Verification failed by business logic
          this.debugInfo += `Status: Failed\nError: ${response.message}\n`;
          this.errorMessage = response.message
            || 'Verification failed. Please try again or contact support.';
        }
      });
  }

  /**
   * Common error handler for verification failures (network, unexpected).
   */
  private handleError(error: any): void {
    console.error('Verification error:', error);
    this.isLoading    = false;
    this.isSuccess    = false;
    this.errorMessage = 'An unexpected error occurred. Please try again.';
    this.debugInfo   += `Status: Error\nDetails: ${JSON.stringify(error)}\n`;
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
