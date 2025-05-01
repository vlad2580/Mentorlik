export interface AuthResponse {
  status: 'success' | 'error';
  message?: string;
  data?: {
    token: string;
    user: {
      id: string;
      email: string;
      firstName: string;
      lastName: string;
      isVerified: boolean;
      userType: 'student' | 'mentor';
      createdAt: string;
      updatedAt: string;
    };
  };
} 