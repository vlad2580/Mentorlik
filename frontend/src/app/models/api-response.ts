// src/app/models/api-response.ts
/**
 * Generic API response wrapper.
 * Used by all endpoints to wrap data, status and messages.
 */
export interface ApiResponse<T> {
    ok: boolean;
    status: 'success' | 'error';
    message: string;
    data?: T;
    timestamp?: string;
    path?: string;
    errors?: ValidationError[];
}

/**
 * Represents a validation error on a specific field
 */
export interface ValidationError {
    field: string;
    message: string;
}  