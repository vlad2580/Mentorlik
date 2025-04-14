import { User } from './user.model';

export interface Mentor extends User {
  expertise: string;
  bio: string;
  experienceYears: number;
  certifications?: string[];
  isAvailable: boolean;
  city?: string;
  country?: string;
  hourlyRate?: number;
  languages?: string[];
  rating?: number;
  reviewCount?: number;
} 