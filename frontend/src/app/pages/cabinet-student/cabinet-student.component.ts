import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { StudentService, Student } from '../../services/student.service';
import { DefaultLayoutComponent } from '../../layouts/default-layout/default-layout.component';

interface Session {
  id: string;
  title: string;
  date: Date;
  time: string;
  mentorName: string;
}

interface LearningGoal {
  name: string;
  progress: number;
}

@Component({
  selector: 'app-cabinet-student',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, DefaultLayoutComponent],
  templateUrl: './cabinet-student.component.html',
  styleUrls: ['./cabinet-student.component.scss']
})
export class CabinetStudentComponent implements OnInit {
  userData: Student | null = null;
  hasNotifications: boolean = false;
  notificationCount: number = 0;
  totalLearningHours: number = 0;
  completedTasks: number = 0;
  rating: number = 0;
  upcomingSessions: Session[] = [];
  learningGoals: LearningGoal[] = [];
  
  constructor(
    private router: Router,
    private studentService: StudentService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.loadDashboardStats();
    this.loadUpcomingSessions();
    this.loadLearningGoals();
  }

  private loadUserData(): void {
    this.studentService.getCurrentStudent().subscribe({
      next: (data: Student) => {
        this.userData = data;
      },
      error: (error: Error) => {
        console.error('Error loading user data:', error);
      }
    });
  }

  private loadDashboardStats(): void {
    // In a real application, these would be loaded from the backend
    this.totalLearningHours = 24;
    this.completedTasks = 12;
    this.rating = 4.5;
    this.hasNotifications = true;
    this.notificationCount = 3;
  }

  private loadUpcomingSessions(): void {
    // Mock data - in real app would come from backend
    this.upcomingSessions = [
      {
        id: '1',
        title: 'JavaScript Fundamentals',
        date: new Date('2024-03-25'),
        time: '15:00',
        mentorName: 'Alex P.'
      },
      {
        id: '2',
        title: 'React Components',
        date: new Date('2024-03-27'),
        time: '16:30',
        mentorName: 'Maria S.'
      }
    ];
  }

  private loadLearningGoals(): void {
    // Mock data - in real app would come from backend
    this.learningGoals = [
      { name: 'JavaScript', progress: 75 },
      { name: 'React', progress: 45 },
      { name: 'Node.js', progress: 30 }
    ];
  }

  logout(): void {
    // Implement logout logic
    this.router.navigate(['/login']);
  }

  editProfile(): void {
    this.router.navigate(['/profile/edit']);
  }

  joinSession(sessionId: string): void {
    // Implement join session logic
    console.log('Joining session:', sessionId);
  }

  scheduleMentoring(): void {
    this.router.navigate(['/schedule-session']);
  }
}
