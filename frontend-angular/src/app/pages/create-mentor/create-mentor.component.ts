import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { MentorService } from '../../services/mentor.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Mentor } from '../../models/mentor.model';

@Component({
  selector: 'app-create-mentor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './create-mentor.component.html',
  styleUrl: './create-mentor.component.scss'
})
export class CreateMentorComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef;
  
  registrationForm!: FormGroup;
  selectedFile: File | null = null;
  previewUrl: string | ArrayBuffer | null = null;
  isDragover = false;
  isLoading = false;
  submitted = false;
  errorMessage: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private mentorService: MentorService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.registrationForm = this.formBuilder.group({
      fullname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      linkedin: [''],
      position: ['', Validators.required],
      company: ['', Validators.required],
      experience: ['', Validators.required],
      specialization: ['', Validators.required],
      skills: ['', Validators.required],
      bio: ['', Validators.required],
      help: ['', Validators.required],
      rate: ['', Validators.required],
      calendar: [''],
      termsConsent: [false, Validators.requiredTrue]
    });
  }

  // удобный геттер для доступа к полям формы
  get f() { 
    return this.registrationForm.controls; 
  }

  // Getters for individual form controls to avoid index signature errors
  get fullnameControl() { return this.registrationForm.get('fullname'); }
  get emailControl() { return this.registrationForm.get('email'); }
  get linkedinControl() { return this.registrationForm.get('linkedin'); }
  get positionControl() { return this.registrationForm.get('position'); }
  get companyControl() { return this.registrationForm.get('company'); }
  get experienceControl() { return this.registrationForm.get('experience'); }
  get specializationControl() { return this.registrationForm.get('specialization'); }
  get skillsControl() { return this.registrationForm.get('skills'); }
  get bioControl() { return this.registrationForm.get('bio'); }
  get helpControl() { return this.registrationForm.get('help'); }
  get rateControl() { return this.registrationForm.get('rate'); }
  get calendarControl() { return this.registrationForm.get('calendar'); }
  get termsConsentControl() { return this.registrationForm.get('termsConsent'); }

  onSubmit() {
    this.submitted = true;
    this.errorMessage = null;

    if (this.registrationForm.invalid) {
      console.error('Form is invalid', this.getFormValidationErrors());
      return;
    }

    if (!this.selectedFile) {
      this.errorMessage = 'Prosím, nahrajte profilovou fotografii.';
      return;
    }

    this.isLoading = true;
    const formValue = this.registrationForm.value;
    
    // Используем только createMentor для всех случаев
    const mentorData = {
      name: formValue.fullname,
      email: formValue.email,
      bio: formValue.bio,
      expertise: formValue.specialization,
      experienceYears: formValue.experience,
      hourlyRate: formValue.rate,
      isAvailable: true,
      password: ''
    } as Mentor;
    
    console.log('Creating mentor with data:', mentorData);
    
    this.mentorService.createMentor(mentorData).subscribe({
      next: (response) => {
        console.log('Mentor created successfully', response);
        
        // Если есть фото и успешно создали ментора, загружаем фото отдельным запросом
        if (this.selectedFile && response.id) {
          this.uploadMentorPhoto(response.id, this.selectedFile);
        } else {
          this.isLoading = false;
          this.router.navigate(['/registration-success']);
        }
      },
      error: (error) => {
        console.error('Error creating mentor', error);
        this.errorMessage = 'Произошла ошибка при создании профиля ментора. Пожалуйста, попробуйте еще раз.';
        this.isLoading = false;
      }
    });
  }
  
  // Новый метод для загрузки фото профиля ментора
  uploadMentorPhoto(mentorId: number, photo: File) {
    const formData = new FormData();
    formData.append('profilePhoto', photo);
    
    this.mentorService.uploadMentorPhoto(mentorId, formData).subscribe({
      next: () => {
        console.log('Profile photo uploaded successfully');
        this.isLoading = false;
        this.router.navigate(['/registration-success']);
      },
      error: (error: any) => {
        console.error('Error uploading profile photo', error);
        // Ментор уже создан, просто покажем предупреждение о проблеме с фото
        this.errorMessage = 'Профиль ментора создан, но возникла проблема при загрузке фото.';
        this.isLoading = false;
        this.router.navigate(['/registration-success']);
      }
    });
  }

  resetForm() {
    this.submitted = false;
    this.registrationForm.reset();
    this.selectedFile = null;
    this.errorMessage = null;
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      this.selectedFile = input.files[0];
    }
  }

  triggerFileInput() {
    this.fileInput.nativeElement.click();
  }

  removeFile() {
    this.selectedFile = null;
    this.fileInput.nativeElement.value = '';
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragover = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragover = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragover = false;
    
    const files = event.dataTransfer?.files;
    if (files && files.length) {
      if (this.isImageFile(files[0])) {
        this.selectedFile = files[0];
      } else {
        alert('Prosím, nahrajte pouze obrázky (JPG, PNG, GIF).');
      }
    }
  }

  isImageFile(file: File): boolean {
    return file.type.startsWith('image/');
  }

  formatFileSize(size: number): string {
    if (size < 1024) {
      return size + ' B';
    } else if (size < 1024 * 1024) {
      return (size / 1024).toFixed(1) + ' KB';
    } else {
      return (size / (1024 * 1024)).toFixed(1) + ' MB';
    }
  }

  getFormValidationErrors() {
    const errors: { [key: string]: string[] } = {};
    Object.keys(this.registrationForm.controls).forEach(key => {
      const control = this.registrationForm.get(key);
      if (control && control.invalid) {
        errors[key] = Object.values(control.errors || {});
      }
    });
    return errors;
  }
}
