import {
  HttpRequest,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (
  request: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const router = inject(Router);
  
  // Получаем токен из localStorage
  const token = localStorage.getItem('auth_token');
  
  // Если токен существует, добавляем его в заголовки запроса
  if (token) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  
  // Обрабатываем ошибки аутентификации
  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      // Если код 401 (Unauthorized) или 403 (Forbidden), перенаправляем на страницу входа
      if (error.status === 401 || error.status === 403) {
        localStorage.removeItem('auth_token');
        localStorage.removeItem('currentUser');
        router.navigate(['/login']);
      }
      
      return throwError(() => error);
    })
  );
}; 