import {
  HttpRequest,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const errorInterceptor: HttpInterceptorFn = (
  request: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  
  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = '';
      
      // Обработка различных типов ошибок
      if (error.error instanceof ErrorEvent) {
        // Клиентская ошибка
        errorMessage = `Ошибка: ${error.error.message}`;
      } else {
        // Серверная ошибка
        switch (error.status) {
          case 400:
            errorMessage = 'Неверный запрос';
            break;
          case 404:
            errorMessage = 'Ресурс не найден';
            break;
          case 500:
            errorMessage = 'Внутренняя ошибка сервера';
            break;
          default:
            errorMessage = `Код ошибки: ${error.status}, Сообщение: ${error.message}`;
        }
      }
      
      // Можно добавить глобальную обработку ошибок здесь, например,
      // отображение уведомлений через сервис уведомлений
      console.error('API Error:', errorMessage, error);
      
      return throwError(() => ({
        error: error.error,
        status: error.status,
        message: errorMessage
      }));
    })
  );
}; 