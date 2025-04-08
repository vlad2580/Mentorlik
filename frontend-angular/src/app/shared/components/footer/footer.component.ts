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
  styleUrl: './footer.component.scss'
})
export class FooterComponent {
  currentYear = new Date().getFullYear();
} 