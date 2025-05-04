import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { StudentService, Student } from '../../services/student.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  userData: Student | null = null;

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.studentService.getCurrentStudent().subscribe({
      next: (data: any) => {
        this.userData = {
          ...data,
          fieldOfStudy: data.fieldOfStudy || data.FieldOfStudy,
          educationLevel: data.educationLevel || data.EducationLevel,
          learningGoals: data.learningGoals || data.LearningGoals,
          skills: data.skills || data.Skills,
          isAvailableForMentorship: data.isAvailableForMentorship ?? data.IsAvailableForMentorship
        };
      },
      error: (error: Error) => {
        console.error('Error loading user data:', error);
      }
    });
  }
} 