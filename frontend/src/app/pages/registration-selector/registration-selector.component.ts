import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-registration-selector',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './registration-selector.component.html',
  styleUrl: './registration-selector.component.css'
})
export class RegistrationSelectorComponent {
  selectedOption: number | null = null;

  constructor(private router: Router) {}

  selectOption(option: number) {
    this.selectedOption = option;
    const buttons = document.querySelectorAll('.select-button');
    buttons.forEach((button, index) => {
      if (index === option - 1) {
        button.classList.add('selected');
      } else {
        button.classList.remove('selected');
      }
    });
  }

  continueRegistration() {
    if (this.selectedOption === 1) {
      // Navigate to student registration
      this.router.navigate(['/create-student']);
    } else if (this.selectedOption === 2) {
      // Navigate to mentor registration
      this.router.navigate(['/create-mentor']);
    }
  }
}
