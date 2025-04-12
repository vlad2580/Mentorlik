import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { StudentService } from '../../services/student.service';

@Component({
  selector: 'app-create-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-student.component.html',
  styleUrl: './create-student.component.scss'
})
export class CreateStudentComponent implements OnInit {
  registrationForm: FormGroup;
  submitted = false;
  errorMessage = '';
  isLoading = false;
  registrationSuccess = false;

  constructor(
    private formBuilder: FormBuilder,
    private studentService: StudentService,
    private router: Router
  ) {
    this.registrationForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      fieldOfStudy: ['', [Validators.required, Validators.maxLength(100)]],
      educationLevel: ['', [Validators.required, Validators.maxLength(50)]],
      learningGoals: ['', [Validators.required, Validators.maxLength(500)]],
      skills: [[]],
      isAvailableForMentorship: [true]
    });
  }

  ngOnInit(): void {
  }

  // Getter for easy access to form fields
  get f() { return this.registrationForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = '';
    
    // Stop here if form is invalid
    if (this.registrationForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.studentService.createStudent(this.registrationForm.value)
      .subscribe({
        next: () => {
          this.isLoading = false;
          this.registrationSuccess = true;
        },
        error: (error) => {
          this.isLoading = false;
          if (error.error && error.error.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'An error occurred during registration. Please try again.';
          }
        }
      });
  }
  
  // Add skill to skills array
  addSkill(skill: string): void {
    const skills = this.registrationForm.get('skills')?.value as string[] || [];
    if (skill && !skills.includes(skill)) {
      skills.push(skill);
      this.registrationForm.patchValue({ skills });
    }
  }
  
  // Remove skill from skills array
  removeSkill(index: number): void {
    const skills = this.registrationForm.get('skills')?.value as string[] || [];
    skills.splice(index, 1);
    this.registrationForm.patchValue({ skills });
  }
}
