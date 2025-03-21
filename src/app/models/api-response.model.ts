export interface ApiResponse<T> {
  status: 'success' | 'error';
  data?: T;
  message?: string;
  timestamp?: string;
  path?: string;
  errors?: ApiValidationError[];
}

export interface ApiValidationError {
  field: string;
  message: string;
} 