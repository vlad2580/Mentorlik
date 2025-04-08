import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { MentorService } from '../../services/mentor.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-mentor-registration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './mentor-registration.component.html',
  styleUrl: './mentor-registration.component.scss'
})
export class MentorRegistrationComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef;
  
  registrationForm!: FormGroup;
  selectedFile: File | null = null;
  isDragover = false;
  isLoading = false;
  submitted = false;
  errorMessage: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private mentorService: MentorService,
    private router: Router
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

    // проверяем валидность формы и наличие фото
    if (this.registrationForm.invalid) {
      this.errorMessage = 'Prosím, vyplňte všechna povinná pole.';
      return;
    }

    if (!this.selectedFile) {
      this.errorMessage = 'Prosím, nahrajte profilovou fotografii.';
      return;
    }

    this.isLoading = true;

    // Создаем объект FormData для отправки файла и данных формы
    const formData = new FormData();
    
    // Добавляем все поля формы
    Object.keys(this.registrationForm.controls).forEach(key => {
      formData.append(key, this.registrationForm.get(key)?.value);
    });
    
    // Добавляем файл
    if (this.selectedFile) {
      formData.append('photo', this.selectedFile);
    }

    // Отправляем данные на сервер
    this.mentorService.registerMentor(formData).subscribe({
      next: (response) => {
        this.isLoading = false;
        // После успешной отправки перенаправляем на страницу подтверждения
        this.router.navigate(['/registration-success']);
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading = false;
        
        if (error.status === 0) {
          // Ошибка соединения
          this.errorMessage = 'Nelze se připojit k serveru. Zkontrolujte své připojení k internetu.';
        } else if (error.status === 400) {
          // Ошибка валидации
          this.errorMessage = error.error?.message || 'Neplatná data. Prosím, zkontrolujte zadané údaje.';
        } else if (error.status === 409) {
          // Конфликт (например, email уже существует)
          this.errorMessage = 'Uživatel s tímto e-mailem již existuje.';
        } else {
          // Другие ошибки
          this.errorMessage = 'Došlo k chybě. Zkuste to prosím znovu později.';
        }
        
        console.error('Registration error:', error);
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
}
