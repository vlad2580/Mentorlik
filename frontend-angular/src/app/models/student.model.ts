import { User } from './user.model';

export interface Student extends User {
  fieldOfStudy: string;
  educationLevel: string;
  learningGoals: string;
  skills?: string[];
  isAvailableForMentorship: boolean;
} 