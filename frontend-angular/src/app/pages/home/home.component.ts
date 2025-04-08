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

  // Method to scroll to the mentors section
  scrollToMentors() {
    const element = document.getElementById('mentors');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  // Method to filter mentors by tags
  filterByTag(tag: string) {
    // Filter implementation will be here
    console.log(`Filtering by tag: ${tag}`);
  }

  // Method to search mentors
  filterMentors(event: Event) {
    const input = (event.target as HTMLInputElement).value.trim().toLowerCase();
    // Search implementation will be here
    console.log(`Searching for: ${input}`);
  }
}
 