import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { MentorCardComponent } from '../../components/mentor-card/mentor-card.component';
import { FaqSectionComponent } from '../../components/faq-section/faq-section.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    FooterComponent,
    MentorCardComponent,
    FaqSectionComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  mentors = [
    {
      name: 'Vladislav Yurikov',
      position: 'Cloud Engineer',
      experience: '10+ let zkušeností',
      price: '9000 Kč',
      imgSrc: 'assets/images/mentor1.jpg',
      tags: 'development, senior, postgres'
    },
    {
      name: 'Zenin Michal',
      position: 'Senior embedded vývojář @ Freelance',
      experience: '5-10 let zkušeností',
      price: 'Zdarma',
      imgSrc: 'assets/images/mentor2.jpg',
      tags: 'embedded, junior, freelance'
    },
    {
      name: 'Tvá fotka zde!',
      position: '',
      experience: '',
      price: '',
      imgSrc: '',
      tags: '',
      isPlaceholder: true
    },
    {
      name: 'Tvá fotka zde!',
      position: '',
      experience: '',
      price: '',
      imgSrc: '',
      tags: '',
      isPlaceholder: true
    }
  ];

  // Метод для прокрутки к разделу менторов
  scrollToMentors() {
    const element = document.getElementById('mentors');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  // Метод для фильтрации менторов по тегам
  filterByTag(tag: string) {
    // Здесь будет реализация фильтрации
    console.log(`Filtering by tag: ${tag}`);
  }

  // Метод для поиска менторов
  filterMentors(event: Event) {
    const input = (event.target as HTMLInputElement).value.trim().toLowerCase();
    // Здесь будет реализация поиска
    console.log(`Searching for: ${input}`);
  }
}
 