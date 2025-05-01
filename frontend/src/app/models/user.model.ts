export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  isVerified: boolean;
  userType: 'student' | 'mentor';
  createdAt: string;
  updatedAt: string;
  password?: string;
} 