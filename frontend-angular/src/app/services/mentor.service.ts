import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Mentor } from '../models/mentor.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MentorService {
  private apiUrl = environment.apiUrl;

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

  // Создание нового ментора через JSON API
  createMentor(mentorData: any): Observable<Mentor> {
    console.log('Creating new mentor with JSON data:', mentorData);
    return this.http.post<Mentor>(`${this.apiUrl}/mentors`, mentorData)
      .pipe(
        tap({
          next: (response) => console.log('Mentor creation successful:', response),
          error: (error) => console.error('Mentor creation error:', error)
        })
      );
  }

  // Загрузка фото профиля ментора
  uploadMentorPhoto(mentorId: number, formData: FormData): Observable<any> {
    console.log(`Uploading photo for mentor ID: ${mentorId}`);
    return this.http.post<any>(`${this.apiUrl}/mentors/${mentorId}/photo`, formData)
      .pipe(
        tap({
          next: (response) => console.log('Photo upload successful:', response),
          error: (error) => console.error('Photo upload error:', error)
        })
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