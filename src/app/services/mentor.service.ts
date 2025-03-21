import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Mentor } from '../models/mentor.model';
import { ApiResponse } from '../models/api-response.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MentorService {
  private apiUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {}

  // Получить список всех менторов
  getAllMentors(): Observable<Mentor[]> {
    return this.http.get<ApiResponse<Mentor[]>>(`${this.apiUrl}/mentors`)
      .pipe(
        map(response => response.data || [])
      );
  }

  // Получить ментора по ID
  getMentorById(id: number): Observable<Mentor> {
    return this.http.get<ApiResponse<Mentor>>(`${this.apiUrl}/mentors/${id}`)
      .pipe(
        map(response => {
          if (!response.data) {
            throw new Error('Ментор не найден');
          }
          return response.data;
        })
      );
  }

  // Поиск менторов
  searchMentors(query: string): Observable<Mentor[]> {
    return this.http.get<ApiResponse<Mentor[]>>(`${this.apiUrl}/mentors/search?query=${query}`)
      .pipe(
        map(response => response.data || [])
      );
  }

  // Обновить данные ментора
  updateMentor(id: number, mentorData: Mentor): Observable<Mentor> {
    return this.http.put<ApiResponse<Mentor>>(`${this.apiUrl}/mentors/${id}`, mentorData)
      .pipe(
        map(response => {
          if (!response.data) {
            throw new Error('Ошибка при обновлении данных ментора');
          }
          return response.data;
        })
      );
  }

  // Удалить ментора
  deleteMentor(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/mentors/${id}`)
      .pipe(
        map(() => {})
      );
  }
} 