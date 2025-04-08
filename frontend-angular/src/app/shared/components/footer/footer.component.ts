import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink],
  template: `
    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-logo">
            <h3>Mentorlik</h3>
            <p>Platforma pro mentoring a vzdělávání</p>
          </div>
          <div class="footer-links">
            <h4>Navigace</h4>
            <ul>
              <li><a routerLink="/">Domů</a></li>
              <li><a routerLink="/blog">Blog</a></li>
              <li><a routerLink="/donate">Podpořte projekt</a></li>
            </ul>
          </div>
          <div class="footer-links">
            <h4>Právní informace</h4>
            <ul>
              <li><a routerLink="/terms">Podmínky použití</a></li>
              <li><a routerLink="/privacy">Zásady ochrany osobních údajů</a></li>
            </ul>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; {{ currentYear }} Mentorlik. Všechna práva vyhrazena.</p>
        </div>
      </div>
    </footer>
  `,
  styles: [`
    .footer {
      background-color: #2c3e50;
      color: #ecf0f1;
      padding: 40px 0 20px;
      margin-top: 60px;
    }
    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 15px;
    }
    .footer-content {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      margin-bottom: 30px;
    }
    .footer-logo {
      flex: 0 0 100%;
      max-width: 100%;
      margin-bottom: 20px;
    }
    .footer-logo h3 {
      font-size: 24px;
      margin: 0 0 10px;
    }
    .footer-logo p {
      margin: 0;
      color: #bdc3c7;
    }
    .footer-links {
      flex: 1 0 auto;
      margin-bottom: 20px;
      min-width: 170px;
    }
    .footer-links h4 {
      font-size: 18px;
      margin: 0 0 15px;
      color: #e74c3c;
    }
    .footer-links ul {
      list-style: none;
      padding: 0;
      margin: 0;
    }
    .footer-links li {
      margin-bottom: 8px;
    }
    .footer-links a {
      color: #ecf0f1;
      text-decoration: none;
      transition: color 0.3s ease;
    }
    .footer-links a:hover {
      color: #3498db;
    }
    .footer-bottom {
      text-align: center;
      padding-top: 20px;
      border-top: 1px solid #34495e;
    }
    .footer-bottom p {
      margin: 0;
      font-size: 14px;
      color: #bdc3c7;
    }
    @media (min-width: 768px) {
      .footer-logo {
        flex: 0 0 30%;
        max-width: 30%;
        margin-bottom: 0;
      }
    }
  `]
})
export class FooterComponent {
  currentYear = new Date().getFullYear();
} 