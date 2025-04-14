import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StudentService } from '../../services/student.service';

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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private studentService: StudentService
  ) {}

  ngOnInit(): void {
    // Get parameters from URL
    this.route.queryParams.subscribe(params => {
      this.userType = params['userType'] || 'student';
      this.verifyEmail();
    });
  }

  verifyEmail(): void {
    // Get token from query parameters
    const token = this.route.snapshot.queryParamMap.get('token');
    
    if (!token) {
      this.isLoading = false;
      this.errorMessage = 'Verification token is missing.';
      return;
    }

    console.log(`Verifying ${this.userType} with token: ${token}`);

    this.studentService.verifyEmail(token).subscribe({
      next: (response) => {
        console.log('Verification response:', response);
        this.isLoading = false;
        this.isSuccess = true;
        // Redirect to login page after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (error) => {
        console.error('Verification error:', error);
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Email verification failed. Please try again.';
      }
    });
  }
}
