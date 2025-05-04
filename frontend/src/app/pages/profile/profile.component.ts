import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudentService, Student } from '../../services/student.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  userData: Student | null = null;

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.studentService.getCurrentStudent().subscribe({
      next: (data: Student) => {
        this.userData = data;
      },
      error: (error: Error) => {
        console.error('Error loading user data:', error);
      }
    });
  }
} 