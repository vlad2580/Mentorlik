import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Student } from '../models/student.model';
import { ApiResponse } from '../models/api-response.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {}

  // Получить список всех студентов
  getAllStudents(): Observable<Student[]> {
    return this.http.get<ApiResponse<Student[]>>(`${this.apiUrl}/students`)
      .pipe(
        map(response => response.data || [])
      );
  }

  // Получить студента по ID
  getStudentById(id: number): Observable<Student> {
    return this.http.get<ApiResponse<Student>>(`${this.apiUrl}/students/${id}`)
      .pipe(
        map(response => {
          if (!response.data) {
            throw new Error('Студент не найден');
          }
          return response.data;
        })
      );
  }

  // Поиск студентов
  searchStudents(query: string): Observable<Student[]> {
    return this.http.get<ApiResponse<Student[]>>(`${this.apiUrl}/students/search?query=${query}`)
      .pipe(
        map(response => response.data || [])
      );
  }

  // Обновить данные студента
  updateStudent(id: number, studentData: Student): Observable<Student> {
    return this.http.put<ApiResponse<Student>>(`${this.apiUrl}/students/${id}`, studentData)
      .pipe(
        map(response => {
          if (!response.data) {
            throw new Error('Ошибка при обновлении данных студента');
          }
          return response.data;
        })
      );
  }

  // Удалить студента
  deleteStudent(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/students/${id}`)
      .pipe(
        map(() => {})
      );
  }
} 