import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cabinet-mentor',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="cabinet-container">
      <h1>Mentor Cabinet</h1>
      <!-- Placeholder for mentor cabinet content -->
    </div>
  `,
  styles: [`
    .cabinet-container {
      padding: 2rem;
    }
  `]
})
export class CabinetMentorComponent {} 