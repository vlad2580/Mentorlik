import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { StudentService } from '../../services/student.service';
import { AuthRequest } from '../../models/auth-request.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  authRequest: AuthRequest = {
    email: '',
    password: ''
  };

  errorMessage: string = '';
  isLoading: boolean = false;
  userType: string = 'student'; // Default to student
  showVerificationOption: boolean = false;
  verificationSuccess: boolean = false;
  isResendingVerification: boolean = false;
  loginSuccess: boolean = false;
  debugResponse: string = '';
  debugInfo: string = '';

  constructor(
    private authService: AuthService,
    private studentService: StudentService,
    private router: Router
  ) {
    console.log('[LOGIN] Component initialized');
    console.log('[LOGIN] Router:', router);
  }

  ngOnInit() {
    // Проверка наличия отладочной информации в localStorage
    this.checkDebugInfo();
  }

  // Переключение типа пользователя
  setUserType(type: string): void {
    this.userType = type;
    this.errorMessage = '';
  }

  // Основной метод для обработки перенаправления после успешного входа
  private handleSuccessfulLogin(responseData: any): void {
    console.log('[LOGIN] Processing login response');

    try {
      // Обновить отладочную информацию
      this.checkDebugInfo();

      // Попробуем сначала использовать данные из localStorage для надежности
      const responseJson = localStorage.getItem('debug_auth_response');
      if (responseJson) {
        try {
          const savedResponse = JSON.parse(responseJson);
          // Работаем с сохраненным ответом, если он есть
          responseData = savedResponse;
        } catch (e) {
          // Если не удалось разобрать JSON, используем исходный responseData
        }
      }

      // Непосредственный анализ структуры ответа
      let userData = null;
      let token = null;

      // Проверяем структуру ответа - вариант с вложенным объектом data
      if (responseData && responseData.status === 'success' && responseData.data) {
        userData = responseData.data;
        token = userData.token;
      }
      // Проверяем прямую структуру с token
      else if (responseData && responseData.token) {
        userData = responseData;
        token = responseData.token;
      }
      // Последняя попытка - искать токен в любом свойстве
      else if (responseData) {
        userData = responseData; // используем весь ответ как данные пользователя

        // Ищем токен в любом свойстве, который выглядит как JWT (начинается с eyJ)
        for (const key in responseData) {
          if (typeof responseData[key] === 'string' && responseData[key].startsWith('eyJ')) {
            token = responseData[key];
            break;
          }
        }
      }

      // Если мы все еще не нашли данные пользователя, пробуем создать минимальные данные
      if (!userData) {
        userData = {
          email: this.authRequest.email,
          userType: this.userType
        };
      }

      // Всегда убеждаемся, что в объекте пользователя есть тип
      userData.userType = this.userType;

      // Если есть токен, добавляем его к данным пользователя
      if (token) {
        userData.token = token;

        // Сохраняем токен для авторизации
        localStorage.setItem('auth_token', token);
        localStorage.setItem('token_timestamp', Date.now().toString());

        // Сохраняем информацию о пользователе
        localStorage.setItem('currentUser', JSON.stringify(userData));

        // Определяем целевой маршрут
        const targetRoute = this.userType === 'mentor'
          ? '/cabinet-mentor'
          : '/cabinet-student';

        // Перенаправляем пользователя на соответствующую страницу
        setTimeout(() => {
          this.executeRedirect(targetRoute);
        }, 500);
      } else {
        throw new Error('Токен не найден в ответе сервера');
      }
    } catch (error: any) {
      console.error('[LOGIN] Ошибка при обработке ответа:', error);
      this.errorMessage = `Ошибка входа: ${error.message}`;
    }
  }

  // New method to try multiple redirect approaches
  private executeRedirect(targetRoute: string): void {
    console.log(`[LOGIN] Executing redirect to ${targetRoute}`);

    // Approach 1: Angular Router with full URL
    try {
      console.log(`[LOGIN] Approach 1: Using Angular Router navigateByUrl with ${targetRoute}`);
      this.router.navigateByUrl(targetRoute)
        .then(() => {
          console.log(`[LOGIN] Router navigation successful to ${targetRoute}`);
          console.log(`[LOGIN] Current URL after navigation: ${window.location.href}`);
        })
        .catch(err => {
          console.error(`[LOGIN] Router navigation failed: ${err.message}`, err);
          this.tryApproach2(targetRoute);
        });
    } catch (navError: any) {
      console.error(`[LOGIN] Router navigation error: ${navError.message}`, navError);
      this.tryApproach2(targetRoute);
    }
  }

  private tryApproach2(targetRoute: string): void {
    // Approach 2: Direct URL manipulation with full path
    console.log(`[LOGIN] Approach 2: Using window.location.href with ${window.location.origin}${targetRoute}`);
    try {
      const fullUrl = window.location.origin + targetRoute;
      console.log(`[LOGIN] Setting window.location.href to ${fullUrl}`);
      window.location.href = fullUrl;

      // Add a fallback check in case the redirect doesn't trigger
      setTimeout(() => {
        console.log(`[LOGIN] Checking if redirect worked, current URL: ${window.location.href}`);
        if (!window.location.href.includes(targetRoute)) {
          console.log('[LOGIN] Redirect may have failed, trying approach 3');
          this.tryApproach3(targetRoute);
        }
      }, 1000);
    } catch (err: any) {
      console.error(`[LOGIN] window.location.href failed: ${err.message}`, err);
      this.tryApproach3(targetRoute);
    }
  }

  private tryApproach3(targetRoute: string): void {
    // Approach 3: location.replace as last resort
    console.log(`[LOGIN] Approach 3: Using window.location.replace with ${window.location.origin}${targetRoute}`);
    try {
      const fullUrl = window.location.origin + targetRoute;
      console.log(`[LOGIN] Using window.location.replace to ${fullUrl}`);
      window.location.replace(fullUrl);
    } catch (err: any) {
      console.error(`[LOGIN] window.location.replace failed: ${err.message}`, err);
      alert('All redirect attempts failed. Please try manually navigating to: ' + targetRoute);
    }
  }

  login(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.showVerificationOption = false;
    this.verificationSuccess = false;
    this.loginSuccess = false;
    this.debugResponse = '';

    console.log('[LOGIN] Attempting login as', this.userType, 'with email:', this.authRequest.email);

    // Clear any previous data
    localStorage.removeItem('auth_token');
    localStorage.removeItem('currentUser');

    // AuthService.login expects (email, password, userType)
    this.authService.login(this.authRequest.email, this.authRequest.password).subscribe({
      next: (response) => {
        console.log('[LOGIN] Successful login response:', response);

        // Debug: Save response to display in UI
        this.debugResponse = 'LOGIN SUCCESS: ' + JSON.stringify(response, null, 2);

        this.isLoading = false;
        this.loginSuccess = true;

        // Process successful login with the API response
        this.handleSuccessfulLogin(response);
      },
      error: (error) => {
        this.isLoading = false;
        this.loginSuccess = false;
        console.error('[LOGIN] Login error:', error);

        // Debug: Save error to display in UI
        this.debugResponse = 'LOGIN ERROR: ' + JSON.stringify(error, null, 2);

        // Enhanced error diagnostics
        if (error && typeof error === 'object') {
          console.error('[LOGIN] Error details:', {
            message: error.message,
            status: error.status,
            name: error.name,
            stack: error.stack
          });
        }

        // Display the error message to the user
        if (typeof error === 'string') {
          this.errorMessage = error;
        } else if (error && error.message) {
          this.errorMessage = error.message;

          if (error.status === 403 && error.message.includes('email')) {
            this.showVerificationOption = true;
          }
        } else {
          this.errorMessage = 'Unknown error during login. Please try again later.';
        }
      }
    });
  }

  // Method to resend verification email
  resendVerificationEmail(): void {
    // Only proceed if we have an email
    if (!this.authRequest.email) {
      this.errorMessage = 'Pro odeslání ověřovacího emailu zadejte svou emailovou adresu.';
      return;
    }

    this.isResendingVerification = true;

    this.studentService.resendVerificationEmail(this.authRequest.email).subscribe({
      next: () => {
        this.isResendingVerification = false;
        this.verificationSuccess = true;

        this.errorMessage = 'Ověřovací email byl odeslán. Prosím, zkontrolujte svou emailovou schránku.';
      },
      error: (error: any) => {
        this.isResendingVerification = false;
        this.errorMessage = error.error?.message || 'Nepodařilo se odeslat ověřovací email. Zkuste to prosím znovu později.';
      }
    });
  }

  // Методы для входа через соцсети, будут реализованы в будущем
  loginWithGoogle(): void {
    this.isLoading = true;
    this.errorMessage = '';

    console.log('[LOGIN] Attempting Google login as', this.userType);

    this.authService.loginWithGoogle('', this.userType).subscribe({
      next: (response: any) => {
        console.log('[LOGIN] Successful Google login:', response);
        this.isLoading = false;
        this.loginSuccess = true;

        // Use the common handler
        this.handleSuccessfulLogin(response);
      },
      error: (error: any) => {
        this.isLoading = false;
        console.error('[LOGIN] Google login error:', error);

        if (typeof error === 'string') {
          this.errorMessage = error;
        } else if (error && error.message) {
          this.errorMessage = error.message;
        } else {
          this.errorMessage = 'Unknown error during Google login. Please try again later.';
        }
      }
    });
  }

  loginWithLinkedin(): void {
    this.isLoading = true;
    this.errorMessage = '';

    console.log('[LOGIN] Attempting LinkedIn login as', this.userType);

    this.authService.loginWithLinkedIn('', this.userType).subscribe({
      next: (response: any) => {
        console.log('[LOGIN] Successful LinkedIn login:', response);
        this.isLoading = false;
        this.loginSuccess = true;

        // Use the common handler
        this.handleSuccessfulLogin(response);
      },
      error: (error: any) => {
        this.isLoading = false;
        console.error('[LOGIN] LinkedIn login error:', error);

        if (typeof error === 'string') {
          this.errorMessage = error;
        } else if (error && error.message) {
          this.errorMessage = error.message;
        } else {
          this.errorMessage = 'Unknown error during LinkedIn login. Please try again later.';
        }
      }
    });
  }

  // Метод для перехода на страницу регистрации
  navigateToRegistration(): void {
    this.router.navigate(['/registration-selector']);
  }

  // Проверяет наличие отладочной информации
  checkDebugInfo() {
    const timestamp = localStorage.getItem('debug_auth_timestamp');
    if (timestamp) {
      const timeStr = new Date(timestamp).toLocaleTimeString();
      this.debugInfo = `Последний запрос авторизации: ${timeStr}\n`;

      // Собираем всю отладочную информацию
      const debugKeys = [
        'debug_auth_usertype',
        'debug_auth_has_data',
        'debug_auth_structure',
        'debug_auth_token_found',
        'debug_auth_keys',
        'debug_auth_error'
      ];

      for (const key of debugKeys) {
        const value = localStorage.getItem(key);
        if (value) {
          this.debugInfo += `${key}: ${value}\n`;
        }
      }

      // Добавляем содержимое полного ответа, если есть
      const responseJson = localStorage.getItem('debug_auth_response');
      if (responseJson) {
        try {
          const response = JSON.parse(responseJson);
          this.debugInfo += `\nОтвет API:\n${JSON.stringify(response, null, 2)}`;
        } catch (e) {
          this.debugInfo += `\nНевозможно распарсить ответ: ${responseJson}`;
        }
      }
    } else {
      this.debugInfo = 'Нет доступной отладочной информации. Попробуйте войти снова.';
    }
  }
}
