import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { MentorCardComponent } from '../../components/mentor-card/mentor-card.component';
import { FaqSectionComponent } from '../../components/faq-section/faq-section.component';

interface Mentor {
  id: number;
  name: string;
  position?: string;
  experience?: string;
  price?: string;
  imgSrc: string;
  tags?: string[];
  isPlaceholder?: boolean;
}

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
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  mentors: Mentor[] = [
    {
      id: 1,
      name: 'Jan Novák',
      position: 'Senior Frontend Developer',
      experience: '8 let zkušeností',
      price: '800 Kč/h',
      imgSrc: 'assets/images/mentor1.jpg',
      tags: ['javascript', 'react', 'development']
    },
    {
      id: 2,
      name: 'Petra Svobodová',
      position: 'UX/UI Designer',
      experience: '6 let zkušeností',
      price: '750 Kč/h',
      imgSrc: 'assets/images/mentor2.jpg',
      tags: ['design', 'ux', 'figma']
    },
    {
      id: 3,
      name: 'Martin Dvořák',
      position: 'DevOps Engineer',
      experience: '10 let zkušeností',
      price: '950 Kč/h',
      imgSrc: 'assets/images/mentor3.jpg',
      tags: ['devops', 'aws', 'docker']
    },
    {
      id: 4,
      name: 'Lucie Procházková',
      position: 'Data Scientist',
      experience: '5 let zkušeností',
      price: '900 Kč/h',
      imgSrc: 'assets/images/mentor4.jpg',
      tags: ['data science', 'python', 'machine learning']
    },
    {
      id: 5,
      name: 'Tomáš Horák',
      position: 'Backend Developer',
      experience: '7 let zkušeností',
      price: '850 Kč/h',
      imgSrc: 'assets/images/mentor5.jpg',
      tags: ['java', 'spring', 'development']
    },
    {
      id: 6,
      name: 'Stanislav Mikula',
      position: 'Mobile Developer',
      experience: '9 let zkušeností',
      price: '880 Kč/h',
      imgSrc: 'assets/images/mentor6.jpg',
      tags: ['android', 'kotlin', 'development']
    },
    {
      id: 7,
      name: 'Staňte se mentorem',
      imgSrc: 'assets/images/placeholder.jpg',
      isPlaceholder: true
    }
  ];

  // Method to scroll to the mentors section
  scrollToMentors() {
    document.getElementById('mentors')?.scrollIntoView({ behavior: 'smooth' });
  }

  // Method to filter mentors by tags
  filterByTag(tag: string) {
    // Filter implementation will be here
    console.log('Filtering by tag:', tag);
  }

  // Method to search mentors
  filterMentors(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.value.toLowerCase().trim();
    
    // Здесь будет реализована фильтрация менторов
    console.log('Filtering mentors by:', value);
  }
}
 