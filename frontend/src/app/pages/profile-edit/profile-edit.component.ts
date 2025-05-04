import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { StudentService, Student } from '../../services/student.service';

@Component({
  selector: 'app-profile-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile-edit.component.html',
  styleUrls: ['./profile-edit.component.scss']
})
export class ProfileEditComponent implements OnInit {
  profileForm!: FormGroup;
  isLoading = false;
  errorMessage: string | null = null;
  userData: Student | null = null;

  constructor(
    private fb: FormBuilder,
    private studentService: StudentService,
    private router: Router
  ) {}

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
        this.initForm();
      },
      error: (error: Error) => {
        this.errorMessage = 'Failed to load profile';
      }
    });
  }

  initForm() {
    this.profileForm = this.fb.group({
      name: [this.userData?.name || '', Validators.required],
      email: [{ value: this.userData?.email || '', disabled: true }],
      fieldOfStudy: [this.userData?.fieldOfStudy || ''],
      educationLevel: [this.userData?.educationLevel || ''],
      learningGoals: [this.userData?.learningGoals || ''],
      skills: [this.userData?.skills ? this.userData.skills.join(', ') : ''],
      isAvailableForMentorship: [!!this.userData?.isAvailableForMentorship]
    });
  }

  onSubmit() {
    if (this.profileForm.invalid) return;
    this.isLoading = true;
    this.errorMessage = null;
    const formValue = this.profileForm.getRawValue();
    const updateData: Partial<Student> = {
      name: formValue.name,
      fieldOfStudy: formValue.fieldOfStudy,
      educationLevel: formValue.educationLevel,
      learningGoals: formValue.learningGoals,
      skills: formValue.skills ? formValue.skills.split(',').map((s: string) => s.trim()) : [],
      isAvailableForMentorship: formValue.isAvailableForMentorship
    };
    this.studentService.updateStudent(this.userData!.id, updateData).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/profile']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to save profile';
      }
    });
  }

  onCancel() {
    this.router.navigate(['/profile']);
  }
} 