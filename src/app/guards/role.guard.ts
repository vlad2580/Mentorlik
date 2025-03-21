import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  // Получаем ожидаемую роль из данных маршрута
  const expectedRole = route.data?.['role'];
  
  if (!authService.isAuthenticated()) {
    // Пользователь не авторизован, перенаправляем на страницу входа
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
  
  const userRole = authService.getUserRole();
  
  if (userRole === expectedRole) {
    // У пользователя правильная роль, пропускаем
    return true;
  }
  
  // У пользователя нет доступа, перенаправляем в соответствии с ролью
  if (userRole === 'student') {
    router.navigate(['/cabinet-student']);
  } else if (userRole === 'mentor') {
    router.navigate(['/cabinet-mentor']);
  } else {
    router.navigate(['/login']);
  }
  
  return false;
}; 