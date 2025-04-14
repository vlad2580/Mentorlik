import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <!-- Хедер был удален, так как используется хедер из страничных компонентов -->
  `,
  styles: []
})
export class HeaderComponent implements OnInit {
  constructor() { }

  ngOnInit(): void {
  }
} 