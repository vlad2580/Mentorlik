import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cabinet-student',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cabinet-student.component.html',
  styleUrl: './cabinet-student.component.css'
})
export class CabinetStudentComponent implements OnInit {
  userData: any = null;
  
  constructor(private router: Router) {}
  
  ngOnInit() {
    console.log('[CABINET] Инициализация кабинета студента');
    
    // Проверяем наличие данных пользователя
    try {
      const userDataStr = localStorage.getItem('currentUser');
      if (userDataStr) {
        this.userData = JSON.parse(userDataStr);
        console.log('[CABINET] Данные пользователя загружены:', this.userData);
      } else {
        console.error('[CABINET] Данные пользователя отсутствуют в localStorage');
        this.redirectToLogin();
      }
    } catch (e) {
      console.error('[CABINET] Ошибка при загрузке данных пользователя:', e);
      this.redirectToLogin();
    }
  }

  logout(): void {
    console.log('[CABINET] Выполняем выход из системы');
    
    // Очистка данных пользователя
    localStorage.removeItem('currentUser');
    localStorage.removeItem('auth_token');
    localStorage.removeItem('token_timestamp');
    
    console.log('[CABINET] Данные пользователя очищены');
    
    // Перенаправление на страницу логина
    this.redirectToLogin();
  }
  
  private redirectToLogin(): void {
    console.log('[CABINET] Перенаправление на страницу входа');
    
    try {
      // Используем window.location для надежного перенаправления
      window.location.href = window.location.origin + '/login';
    } catch (e) {
      console.error('[CABINET] Ошибка при перенаправлении:', e);
      this.router.navigateByUrl('/login');
    }
  }
}
