import { z } from 'zod';

export const createUserSchema = z.object({
    name: z.string()
        .min(2, 'Name must be between 2 and 100 characters')
        .max(100, 'Name must be between 2 and 100 characters')
        .regex(/^[A-Za-z][A-Za-z\s.'-]*$/, 'Name must contain only letters, spaces, dots, apostrophes and hyphens'),
    email: z.string().email('Email must be a valid email address'),
    phone: z.string()
        .min(10, 'Phone must be between 10 and 15 characters')
        .max(15, 'Phone must be between 10 and 15 characters')
        .regex(/^[0-9+\-() ]+$/, 'Phone must contain only numbers, plus signs, parentheses, spaces and hyphens'),
    password: z.string()
        .min(8, 'Password must be between 8 and 50 characters')
        .max(50, 'Password must be between 8 and 50 characters'),
    role: z.enum(['USER', 'OWNER', 'ADMIN']).default('USER')
});

export const loginSchema = z.object({
    email: z.string().email('Email must be a valid email address'),
    password: z.string().min(1, 'Password is required')
});

export const updateUserSchema = z.object({
    name: z.string()
        .min(2, 'Name must be between 2 and 100 characters')
        .max(100, 'Name must be between 2 and 100 characters')
        .regex(/^[A-Za-z][A-Za-z\s.'-]*$/, 'Name must contain only letters, spaces, dots, apostrophes and hyphens'),
    email: z.string().email('Email must be a valid email address'),
    phone: z.string()
        .min(10, 'Phone must be between 10 and 15 characters')
        .max(15, 'Phone must be between 10 and 15 characters')
        .regex(/^[0-9+\-() ]+$/, 'Phone must contain only numbers, plus signs, parentheses, spaces and hyphens'),
    password: z.string()
        .min(8, 'Password must be between 8 and 50 characters')
        .max(50, 'Password must be between 8 and 50 characters')
        .optional(),
    role: z.enum(['USER', 'OWNER', 'ADMIN']),
    status: z.enum(['ACTIVE', 'INACTIVE'])
});

export const bookingLogSchema = z.object({
    parkingId: z.string().min(1, 'Parking id is required'),
    vehicleId: z.string().min(1, 'Vehicle id is required'),
    action: z.enum(['RESERVED', 'RELEASED'])
});

export const userIdSchema = z.object({
    id: z.string().regex(/^[0-9a-fA-F]{24}$/, 'Invalid user id')
});

export type CreateUserInput = z.infer<typeof createUserSchema>;
export type LoginInput = z.infer<typeof loginSchema>;
export type UpdateUserInput = z.infer<typeof updateUserSchema>;
export type BookingLogInput = z.infer<typeof bookingLogSchema>;
