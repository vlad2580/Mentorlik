import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { BehaviorSubject, Observable, tap } from 'rxjs';

interface User {
  id: string;
  email: string;
  name: string;
  role: string;
}

interface AuthResponse {
  user: User;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  
  constructor(private apiService: ApiService) {
    // Try to restore session when initializing the service
    this.loadUserFromStorage();
  }
  
  login(email: string, password: string): Observable<AuthResponse> {
    return this.apiService.post<AuthResponse>('auth/login', { email, password })
      .pipe(
        tap(response => {
          // Save token and user information
          localStorage.setItem('auth_token', response.token);
          localStorage.setItem('user', JSON.stringify(response.user));
          this.currentUserSubject.next(response.user);
        })
      );
  }
  
  register(userData: any): Observable<AuthResponse> {
    return this.apiService.post<AuthResponse>('auth/register', userData)
      .pipe(
        tap(response => {
          localStorage.setItem('auth_token', response.token);
          localStorage.setItem('user', JSON.stringify(response.user));
          this.currentUserSubject.next(response.user);
        })
      );
  }
  
  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }
  
  isAuthenticated(): boolean {
    return !!localStorage.getItem('auth_token');
  }
  
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }
  
  private loadUserFromStorage(): void {
    const userJson = localStorage.getItem('user');
    if (userJson) {
      try {
        const user = JSON.parse(userJson);
        this.currentUserSubject.next(user);
      } catch (e) {
        // In case of JSON parsing error, clear data
        this.logout();
      }
    }
  }
} 