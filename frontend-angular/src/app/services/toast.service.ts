import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  constructor(private toastr: ToastrService) {}

  /**
   * Show success notification
   */
  success(message: string, title: string = 'Success'): void {
    this.toastr.success(message, title, {
      timeOut: 5000, // Increase display time to 5 seconds
      progressBar: true
    });
  }

  /**
   * Show error notification
   */
  error(message: string, title: string = 'Error'): void {
    this.toastr.error(message, title);
  }

  /**
   * Show info notification
   */
  info(message: string, title: string = 'Information'): void {
    this.toastr.info(message, title);
  }

  /**
   * Show warning notification
   */
  warning(message: string, title: string = 'Warning'): void {
    this.toastr.warning(message, title);
  }
} 