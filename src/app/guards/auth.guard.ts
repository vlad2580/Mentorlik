import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    // Пользователь авторизован, пропускаем
    return true;
  }

  // Пользователь не авторизован, перенаправляем на страницу входа
  router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
  return false;
}; 