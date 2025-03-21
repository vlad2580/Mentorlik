import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface FaqItem {
  question: string;
  answer: string;
  isOpen: boolean;
}

@Component({
  selector: 'app-faq-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './faq-section.component.html',
  styleUrl: './faq-section.component.css'
})
export class FaqSectionComponent {
  faqItems: FaqItem[] = [
    {
      question: 'What is your return policy?',
      answer: 'Our return policy allows returns within 30 days of purchase. Please ensure the item is in its original condition.',
      isOpen: false
    },
    {
      question: 'Do you offer international shipping?',
      answer: 'Yes, we ship to many countries worldwide. Shipping fees and times vary based on location.',
      isOpen: false
    },
    {
      question: 'How can I track my order?',
      answer: 'Once your order is shipped, you will receive a tracking link via email to monitor its delivery status.',
      isOpen: false
    }
  ];

  toggleFaq(item: FaqItem): void {
    item.isOpen = !item.isOpen;
  }
} 