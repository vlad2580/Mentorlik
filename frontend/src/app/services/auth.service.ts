import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthRequest, OAuth2Request } from '../models/auth-request.model';
import { User } from '../models/user.model';
import { Mentor } from '../models/mentor.model';
import { Student } from '../models/student.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUser: User | null = null;
  isLoggedIn = signal<boolean>(false);

  constructor(private http: HttpClient) {
    // Проверяем, есть ли сохраненный пользователь в localStorage
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUser = JSON.parse(savedUser);
    }
    this.isLoggedIn.set(!!localStorage.getItem('auth_token'));
  }

  //workaround 
  login(email: string, password: string): Observable<User | Mentor | Student> {
    return this.http.post<User | Mentor | Student>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        tap(user => {
          this.setCurrentUser(user);
        })
      );
  }

  // Аутентификация через Google
  loginWithGoogle(userType: string, token: string): Observable<User | Mentor | Student> {
    return this.http.get<User | Mentor | Student>(
      `${this.apiUrl}/oauth2/google?userType=${userType}&token=${token}`
    ).pipe(
      tap(user => {
        this.setCurrentUser(user);
      })
    );
  }

  // Аутентификация через LinkedIn
  loginWithLinkedIn(userType: string, token: string): Observable<User | Mentor | Student> {
    return this.http.get<User | Mentor | Student>(
      `${this.apiUrl}/oauth2/linkedin?userType=${userType}&token=${token}`
    ).pipe(
      tap(user => {
        this.setCurrentUser(user);
      })
    );
  }

  // Регистрация нового пользователя
  register(userType: string, userData: User | Mentor | Student): Observable<User | Mentor | Student> {
    return this.http.post<User | Mentor | Student>(`${this.apiUrl}/register/${userType}`, userData)
      .pipe(
        tap(user => {
          this.setCurrentUser(user);
        })
      );
  }

  // Выход из системы
  logout(): void {
    this.currentUser = null;
    localStorage.removeItem('currentUser');
    localStorage.removeItem('auth_token');
    this.isLoggedIn.set(false);
  }

  // Получить текущего пользователя
  getCurrentUser(): User | null {
    return this.currentUser;
  }

  // Проверить, аутентифицирован ли пользователь
  isAuthenticated(): boolean {
    return !!this.currentUser;
  }

  // Получить роль пользователя (mentor, student)
  getUserRole(): string | null {
    if (!this.currentUser) return null;
    
    if ('expertise' in this.currentUser) {
      return 'mentor';
    } else if ('fieldOfStudy' in this.currentUser) {
      return 'student';
    }
    
    return null;
  }

  // Сохранить пользователя в localStorage и в сервисе
  private setCurrentUser(user: User | Mentor | Student): void {
    this.currentUser = user;
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  setToken(token: string) {
    localStorage.setItem('auth_token', token);
    this.isLoggedIn.set(true);
  }
} 