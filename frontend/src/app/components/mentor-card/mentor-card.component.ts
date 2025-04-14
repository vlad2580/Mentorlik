import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

interface Mentor {
  id?: number;
  name: string;
  position?: string;
  experience?: string;
  price?: string;
  imgSrc: string;
  tags?: string[];
  isPlaceholder?: boolean;
}

@Component({
  selector: 'app-mentor-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mentor-card.component.html',
  styleUrl: './mentor-card.component.scss'
})
export class MentorCardComponent {
  @Input() mentor: Mentor = {
    name: '',
    imgSrc: ''
  };
} 