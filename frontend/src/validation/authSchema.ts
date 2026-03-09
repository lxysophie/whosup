import { z } from 'zod';

export const loginSchema = z.object({
  email: z.string().email('Invalid email address'),
  password: z.string().min(1, 'Password is required'),
});

export type LoginFormData = z.infer<typeof loginSchema>;

export const registerSchema = z.object({
  email: z.string().email('Invalid email address').max(255),
  password: z.string().min(8, 'Password must be at least 8 characters').max(72),
  displayName: z
    .string()
    .min(2, 'Display name must be at least 2 characters')
    .max(100),
});

export type RegisterFormData = z.infer<typeof registerSchema>;
