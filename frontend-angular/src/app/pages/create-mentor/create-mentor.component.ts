import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { MentorService } from '../../services/mentor.service';
import { ToastService } from '../../services/toast.service';

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
  showSuccessModal = false; // Флаг для отображения модального окна успеха

  constructor(
    private formBuilder: FormBuilder,
    private mentorService: MentorService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService
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
    console.log('Form values:', formValue);
    
    // Конвертируем файл в base64
    const reader = new FileReader();
    const file = this.selectedFile; // Создаем локальную переменную, которая точно не null
    console.log('Selected file:', file.name, file.type, file.size);
    
    reader.readAsDataURL(file);
    reader.onload = () => {
      const base64 = reader.result as string;
      console.log('File converted to base64, length:', base64.length);
      const base64Data = base64.split(',')[1]; // Убираем префикс "data:image/jpeg;base64,"
      
      // Создаем данные для отправки, включая фото как base64
      const mentorData = {
        fullname: formValue.fullname, // Добавляем standard backend names
        name: formValue.fullname,
        email: formValue.email,
        bio: formValue.bio,
        about: formValue.bio,
        expertise: formValue.specialization,
        specialization: formValue.specialization,
        experienceYears: formValue.experience,
        experience: formValue.experience,
        hourlyRate: formValue.rate,
        rate: formValue.rate,
        isAvailable: true,
        password: '',
        linkedin: formValue.linkedin,
        position: formValue.position,
        company: formValue.company,
        skills: formValue.skills,
        help: formValue.help,
        calendar: formValue.calendar,
        termsConsent: formValue.termsConsent,
        photoBase64: base64Data, // Отправляем как photoBase64
        photoContentType: file.type
      };
      
      console.log('Creating mentor with data (including base64 photo):', {
        ...mentorData,
        photoBase64: `[base64 data with length ${base64Data.length}]` // Не логируем полный base64 чтобы не засорять консоль
      });
      
      this.mentorService.createMentor(mentorData).subscribe({
        next: (response) => {
          console.log('Mentor created successfully', response);
          this.isLoading = false;
          
          // Показываем уведомление Toast с увеличенным временем отображения
          this.toastService.success('Žádost o mentorství byla úspěšně odeslána!', 'Úspěch!');
          
          // Показываем модальное окно успеха вместо редиректа
          this.showSuccessModal = true;
          
          // Очищаем форму
          this.resetForm();
          
          // Опциональное перенаправление на главную страницу через 5 секунд
          setTimeout(() => {
            this.router.navigate(['/']);
          }, 5000);
        },
        error: (error) => {
          console.error('Error creating mentor', error);
          this.isLoading = false;
          
          if (error.error && error.error.message) {
            this.errorMessage = error.error.message;
            this.toastService.error(error.error.message, 'Chyba!');
          } else {
            this.errorMessage = 'Произошла ошибка при создании профиля ментора. Пожалуйста, попробуйте еще раз.';
            this.toastService.error('Došlo k neočekávané chybě. Prosím, zkuste to znovu později.', 'Chyba!');
          }
        }
      });
    };
    
    reader.onerror = (error) => {
      console.error('Error reading file:', error);
      this.errorMessage = 'Произошла ошибка при чтении файла фотографии.';
      this.isLoading = false;
      this.toastService.error('Chyba při čtení souboru fotografie.', 'Chyba!');
    };
  }

  // Закрыть модальное окно успеха
  closeSuccessModal() {
    this.showSuccessModal = false;
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
