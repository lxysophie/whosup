import client from './client';
import type { Activity, ActivityDetail } from '../types/activity';
import type { Page } from '../types/api';

export interface ActivityFilters {
  page?: number;
  size?: number;
  status?: string;
  category?: string;
  search?: string;
}

export interface CreateActivityData {
  title: string;
  description?: string;
  location: string;
  activityDate: string;
  category: string;
  maxParticipants: number;
}

export const getActivities = (filters: ActivityFilters = {}) =>
  client.get<Page<Activity>>('/activities', { params: filters }).then((r) => r.data);

export const getActivity = (id: number) =>
  client.get<ActivityDetail>(`/activities/${id}`).then((r) => r.data);

export const createActivity = (data: CreateActivityData) =>
  client.post<ActivityDetail>('/activities', data).then((r) => r.data);

export const updateActivity = (id: number, data: Partial<CreateActivityData>) =>
  client.put<ActivityDetail>(`/activities/${id}`, data).then((r) => r.data);

export const deleteActivity = (id: number) =>
  client.delete(`/activities/${id}`);

export const cancelActivity = (id: number) =>
  client.patch<ActivityDetail>(`/activities/${id}/cancel`).then((r) => r.data);

export const completeActivity = (id: number) =>
  client.patch<ActivityDetail>(`/activities/${id}/complete`).then((r) => r.data);

export const joinActivity = (id: number) =>
  client.post(`/activities/${id}/join`);

export const leaveActivity = (id: number) =>
  client.delete(`/activities/${id}/leave`);

export const getMyJoinedActivities = () =>
  client.get<Activity[]>('/users/me/activities').then((r) => r.data);

export const getMyCreatedActivities = () =>
  client.get<Activity[]>('/users/me/created').then((r) => r.data);
