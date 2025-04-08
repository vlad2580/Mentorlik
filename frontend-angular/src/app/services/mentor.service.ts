import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Mentor } from '../models/mentor.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MentorService {
  private apiUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {
    console.log('MentorService initialized with API URL:', this.apiUrl);
  }

  // Получить список всех менторов
  getAllMentors(): Observable<Mentor[]> {
    return this.http.get<Mentor[]>(`${this.apiUrl}/mentors`);
  }

  // Получить ментора по ID
  getMentorById(id: number): Observable<Mentor> {
    return this.http.get<Mentor>(`${this.apiUrl}/mentors/${id}`);
  }

  // Поиск менторов
  searchMentors(query: string): Observable<Mentor[]> {
    return this.http.get<Mentor[]>(`${this.apiUrl}/mentors/search?query=${query}`);
  }

  // Регистрация нового ментора
  registerMentor(formData: FormData): Observable<any> {
    console.log('Sending mentor registration request to:', `${this.apiUrl}/mentor-registration`);
    // Получаем список ключей FormData
    const formDataKeys: string[] = [];
    formData.forEach((value, key) => {
      formDataKeys.push(key);
    });
    console.log('FormData keys:', formDataKeys);
    
    return this.http.post<any>(`${this.apiUrl}/mentor-registration`, formData)
      .pipe(
        tap(
          response => console.log('Registration successful:', response),
          error => console.error('Registration error:', error)
        )
      );
  }

  // Обновить данные ментора
  updateMentor(id: number, mentorData: Mentor): Observable<Mentor> {
    return this.http.put<Mentor>(`${this.apiUrl}/mentors/${id}`, mentorData);
  }

  // Удалить ментора
  deleteMentor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/mentors/${id}`);
  }
} 