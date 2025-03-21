import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mentor } from '../models/mentor.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MentorService {
  private apiUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {}

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

  // Обновить данные ментора
  updateMentor(id: number, mentorData: Mentor): Observable<Mentor> {
    return this.http.put<Mentor>(`${this.apiUrl}/mentors/${id}`, mentorData);
  }

  // Удалить ментора
  deleteMentor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/mentors/${id}`);
  }
} 