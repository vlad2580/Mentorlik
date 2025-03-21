import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-mentor-card',
  standalone: true,
  imports: [],
  templateUrl: './mentor-card.component.html',
  styleUrl: './mentor-card.component.css'
})
export class MentorCardComponent {
  @Input() mentor: {
    name: string;
    position: string;
    experience: string;
    price: string;
    imgSrc: string;
    tags: string;
    isPlaceholder?: boolean;
  } = {
    name: '',
    position: '',
    experience: '',
    price: '',
    imgSrc: '',
    tags: ''
  };
} 