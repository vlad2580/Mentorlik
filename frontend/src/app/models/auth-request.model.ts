export interface AuthRequest {
  email: string;
  password: string;
}

export interface OAuth2Request {
  token: string;
  userType: 'mentor' | 'student';
} 