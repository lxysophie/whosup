import client from './client';
import type { AuthResponse, User } from '../types/user';

export const login = (email: string, password: string) =>
  client.post<AuthResponse>('/auth/login', { email, password }).then((r) => r.data);

export const register = (email: string, password: string, displayName: string) =>
  client
    .post<AuthResponse>('/auth/register', { email, password, displayName })
    .then((r) => r.data);

export const getMe = () =>
  client.get<User>('/auth/me').then((r) => r.data);
