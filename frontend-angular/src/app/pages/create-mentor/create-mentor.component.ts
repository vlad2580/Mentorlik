import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { MentorService } from '../../services/mentor.service';
import { ToastService } from '../../services/toast.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

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
  showSuccessModal = false; // Flag for displaying the success modal

  constructor(
    private formBuilder: FormBuilder,
    private mentorService: MentorService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService,
    private http: HttpClient
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

  // Convenient getter for accessing form fields
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
    
    // Добавим диагностический вывод перед отправкой
    console.log('Starting file to base64 conversion');
    
    // Convert file to base64
    const reader = new FileReader();
    const file = this.selectedFile; // Create a local variable that is definitely not null
    console.log('Selected file:', file.name, file.type, file.size + ' bytes');
    
    reader.readAsDataURL(file);
    reader.onload = () => {
      try {
        console.log('FileReader onload triggered');
        const base64 = reader.result as string;
        console.log('File converted to base64, length:', base64.length);
        
        // Проверка содержимого base64
        if (!base64 || base64.length < 100) {
          throw new Error('Invalid base64 content');
        }
        
        const base64Parts = base64.split(',');
        if (base64Parts.length < 2) {
          throw new Error('Invalid base64 format');
        }
        
        const base64Data = base64Parts[1]; // Remove prefix "data:image/jpeg;base64,"
        
        // Create data for submission, including photo as base64
        const mentorData = {
          fullname: formValue.fullname, // Add standard backend names
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
          photoBase64: base64Data, // Send as photoBase64
          photoContentType: file.type
        };
        
        // Дополнительная проверка структуры данных перед отправкой
        const requiredFields = ['fullname', 'email', 'bio', 'specialization', 'experience', 'rate', 'photoBase64', 'photoContentType'];
        const missingFields = requiredFields.filter(field => {
          return !mentorData[field as keyof typeof mentorData];
        });
        
        if (missingFields.length > 0) {
          throw new Error(`Missing required fields: ${missingFields.join(', ')}`);
        }
        
        console.log('Creating mentor with data:', {
          ...mentorData,
          photoBase64: `[base64 data with length ${base64Data.length}]` // Don't log full base64 to avoid cluttering the console
        });
        
        // Проверка соединения перед отправкой
        this.http.get(environment.apiUrl + '/mentors').subscribe({
          next: () => {
            console.log('API is reachable, proceeding with mentor creation');
            this.proceedWithMentorCreation(mentorData);
          },
          error: (error) => {
            console.error('API is not reachable:', error);
            this.isLoading = false;
            this.errorMessage = 'Nelze se připojit k serveru. Zkontrolujte prosím své připojení k internetu a zkuste to znovu.';
            this.toastService.error(this.errorMessage, 'Chyba připojení!');
          }
        });
      } catch (e) {
        console.error('Error processing the image:', e);
        this.isLoading = false;
        this.errorMessage = 'Chyba při zpracování obrázku. Zkuste jiný soubor.';
        this.toastService.error(this.errorMessage, 'Error!');
      }
    };
    
    reader.onerror = (error) => {
      console.error('Error reading file:', error);
      this.errorMessage = 'An error occurred while reading the photo file.';
      this.isLoading = false;
      this.toastService.error('Error reading photo file.', 'Error!');
    };
  }

  // Новый метод для продолжения создания ментора после проверки соединения
  proceedWithMentorCreation(mentorData: any) {
    console.log('Calling mentor service to create mentor');
    this.mentorService.createMentor(mentorData).subscribe({
      next: (response) => {
        console.log('Mentor created successfully', response);
        this.isLoading = false;
        
        // Show Toast notification with increased display time
        this.toastService.success('Žádost o mentorství byla úspěšně odeslána!', 'Úspěch!');
        
        // Show success modal instead of redirect
        this.showSuccessModal = true;
        
        // Clear form
        this.resetForm();
        
        // Optional redirect to home page after 5 seconds
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 5000);
      },
      error: (error) => {
        console.error('Error creating mentor', error);
        this.isLoading = false;
        
        // Расширенное логирование для отладки
        console.error('Error details:', {
          status: error.status,
          statusText: error.statusText,
          url: error.url,
          errorBody: error.error,
          headers: error.headers?.keys ? Array.from(error.headers.keys()).map(key => `${key}: ${error.headers.get(key)}`).join(', ') : 'No headers'
        });
        
        let errorMsg = 'An error occurred while creating the mentor profile. Please try again.';
        
        // Get error message from API response
        if (error.error && error.error.message) {
          errorMsg = error.error.message;
          
          // Special handling for photo format errors
          if (errorMsg.includes('Invalid photo format')) {
            errorMsg = 'Nepodporovaný formát fotografie. Nahrajte prosím obrázek ve formátu JPEG nebo PNG.';
          }
        }
        
        this.errorMessage = errorMsg;
        this.toastService.error(errorMsg, 'Error!');
      }
    });
  }

  // Close success modal
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
      const file = input.files[0];
      
      // Проверка типа файла
      if (!this.isAllowedImageType(file.type)) {
        this.errorMessage = 'Nepodporovaný formát fotografie. Povolené formáty jsou: JPEG, PNG.';
        this.toastService.error(this.errorMessage, 'Error!');
        this.selectedFile = null;
        return;
      }
      
      // Проверка размера файла (максимум 5 МБ)
      const maxSizeInBytes = 5 * 1024 * 1024; // 5 МБ
      if (file.size > maxSizeInBytes) {
        this.errorMessage = 'Soubor je příliš velký. Maximální velikost je 5 MB.';
        this.toastService.error(this.errorMessage, 'Error!');
        this.selectedFile = null;
        return;
      }
      
      console.log('Selected file:', file.name, file.type, file.size + ' bytes');
      this.selectedFile = file;
      
      // Создаем превью изображения
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  // Проверяет, является ли тип файла разрешенным форматом изображения
  isAllowedImageType(mimeType: string): boolean {
    const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg'];
    return allowedTypes.includes(mimeType);
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
