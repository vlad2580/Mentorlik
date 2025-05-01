import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { catchError, tap } from 'rxjs/operators';
import { ApiResponse } from '../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Create student (public registration)
  createStudent(studentData: any): Observable<any> {
    console.log('Creating new student:', studentData);
    return this.http.post<any>(`${this.apiUrl}/public/students/register`, studentData)
      .pipe(
        tap({
          next: (response) => console.log('Student creation successful:', response),
          error: (error) => {
            console.error('Student creation error:', error);
            console.error('Error status:', error.status);
            console.error('Error message:', error.message);
            console.error('Error details:', error.error);
            console.error('Full error object:', JSON.stringify(error));
          }
        })
      );
  }

  /**
   * Verify student email
   */
  verifyEmail(token: string): Observable<ApiResponse<void>> {
    return this.http
      .get<ApiResponse<void>>(
        `${environment.apiUrl}/public/students/verify`,
        { params: { token } }
      )
      .pipe(
        tap(response => {
          console.log('Email verification response:', response);
        }),
        catchError(err => {
          console.error('Email verification error:', err);
          return throwError(() => err);
        })
      );
  }

  // Get all students (admin only)
  getAllStudents(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/students`)
      .pipe(
        tap({
          next: (response) => console.log('Retrieved students:', response),
          error: (error) => console.error('Error getting students:', error)
        })
      );
  }

  // Get student by ID (admin or owner)
  getStudentById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/admin/students/${id}`)
      .pipe(
        tap({
          next: (response) => console.log('Retrieved student:', response),
          error: (error) => console.error('Error getting student:', error)
        })
      );
  }

  // Update student (owner only)
  updateStudent(id: number, studentData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/admin/students/${id}`, studentData)
      .pipe(
        tap({
          next: (response) => console.log('Student update successful:', response),
          error: (error) => console.error('Student update error:', error)
        })
      );
  }

  // Delete student (admin or owner)
  deleteStudent(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/admin/students/${id}`)
      .pipe(
        tap({
          next: (response) => console.log('Student deletion successful:', response),
          error: (error) => console.error('Student deletion error:', error)
        })
      );
  }

  // Resend verification email
  resendVerificationEmail(email: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/public/students/resend-verification`, { email })
      .pipe(
        tap({
          next: (response) => console.log('Verification email resent:', response),
          error: (error) => console.error('Error resending verification email:', error)
        })
      );
  }
}