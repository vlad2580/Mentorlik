import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registration-selector',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './registration-selector.component.html',
  styleUrl: './registration-selector.component.css'
})
export class RegistrationSelectorComponent {
  selectedOption: number | null = null;

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
}
