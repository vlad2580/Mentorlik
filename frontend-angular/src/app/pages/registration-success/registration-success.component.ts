import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-registration-success',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="success-container">
      <div class="success-card">
        <div class="success-icon">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="64" height="64">
            <circle cx="12" cy="12" r="10" fill="#4CAF50"/>
            <path fill="#FFFFFF" d="M10 17l-5-5 1.41-1.42L10 14.17l7.59-7.59L19 8z"/>
          </svg>
        </div>

        <h1>Žádost o mentorství byla úspěšně odeslána!</h1>
        
        <p>
          Děkujeme za Váš zájem stát se mentorem v naší komunitě. 
          Váš požadavek bude přezkoumán naším týmem a budeme Vás kontaktovat 
          co nejdříve (obvykle do 3 pracovních dnů).
        </p>
        
        <p>
          Mezitím si můžete prohlédnout náš 
          <a routerLink="/blog">blog</a> s užitečnými radami a tipy pro mentory.
        </p>
        
        <div class="action-buttons">
          <a routerLink="/" class="btn-primary">Zpět na hlavní stránku</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .success-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 80vh;
      padding: 2rem;
      background-color: #f5f7fa;
    }
    
    .success-card {
      max-width: 600px;
      padding: 3rem;
      background-color: white;
      border-radius: 12px;
      box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
      text-align: center;
    }
    
    .success-icon {
      margin-bottom: 2rem;
    }
    
    h1 {
      font-size: 1.8rem;
      color: #2c3e50;
      margin-bottom: 1.5rem;
    }
    
    p {
      color: #546e7a;
      line-height: 1.6;
      margin-bottom: 1.5rem;
    }
    
    .action-buttons {
      margin-top: 2rem;
    }
    
    .btn-primary {
      display: inline-block;
      padding: 0.8rem 1.5rem;
      background-color: #4a6da7;
      color: white;
      text-decoration: none;
      border-radius: 6px;
      font-weight: 600;
      transition: all 0.3s ease;
    }
    
    .btn-primary:hover {
      background-color: #3a5a8c;
    }
    
    a {
      color: #4a6da7;
      font-weight: 500;
      text-decoration: none;
    }
    
    a:hover {
      text-decoration: underline;
    }
  `]
})
export class RegistrationSuccessComponent {} 