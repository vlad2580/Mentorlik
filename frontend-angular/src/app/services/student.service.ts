import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Register a new student
  registerStudent(studentData: any): Observable<any> {
    console.log('Registering new student:', studentData);
    return this.http.post<any>(`${this.apiUrl}/students/register`, studentData)
      .pipe(
        tap({
          next: (response) => console.log('Student registration successful:', response),
          error: (error) => console.error('Student registration error:', error)
        })
      );
  }

  // Create student (new method name for registerStudent)
  createStudent(studentData: any): Observable<any> {
    console.log('Creating new student:', studentData);
    return this.http.post<any>(`${this.apiUrl}/students/create-student`, studentData)
      .pipe(
        tap({
          next: (response) => console.log('Student creation successful:', response),
          error: (error) => console.error('Student creation error:', error)
        })
      );
  }

  // Verify student email
  verifyEmail(token: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/students/register/verify?token=${token}`)
      .pipe(
        tap({
          next: (response) => console.log('Email verification successful:', response),
          error: (error) => console.error('Email verification error:', error)
        })
      );
  }

  // Get all students
  getAllStudents(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/students`)
      .pipe(
        tap({
          next: (response) => console.log('Retrieved students:', response),
          error: (error) => console.error('Error getting students:', error)
        })
      );
  }

  // Get student by ID
  getStudentById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/students/${id}`)
      .pipe(
        tap({
          next: (response) => console.log('Retrieved student:', response),
          error: (error) => console.error('Error getting student:', error)
        })
      );
  }

  // Update student
  updateStudent(id: number, studentData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/students/${id}`, studentData)
      .pipe(
        tap({
          next: (response) => console.log('Student update successful:', response),
          error: (error) => console.error('Student update error:', error)
        })
      );
  }

  // Delete student
  deleteStudent(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/students/${id}`)
      .pipe(
        tap({
          next: (response) => console.log('Student deletion successful:', response),
          error: (error) => console.error('Student deletion error:', error)
        })
      );
  }
} 