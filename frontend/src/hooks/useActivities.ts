import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import * as api from '../api/activities';

export function useActivities(filters: api.ActivityFilters = {}) {
  return useQuery({
    queryKey: ['activities', filters],
    queryFn: () => api.getActivities(filters),
  });
}

export function useActivity(id: number) {
  return useQuery({
    queryKey: ['activity', id],
    queryFn: () => api.getActivity(id),
  });
}

export function useCreateActivity() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: api.createActivity,
    onSuccess: () => qc.invalidateQueries({ queryKey: ['activities'] }),
  });
}

export function useUpdateActivity(id: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: Partial<api.CreateActivityData>) => api.updateActivity(id, data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['activities'] });
      qc.invalidateQueries({ queryKey: ['activity', id] });
    },
  });
}

export function useDeleteActivity() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: api.deleteActivity,
    onSuccess: () => qc.invalidateQueries({ queryKey: ['activities'] }),
  });
}

export function useCancelActivity(id: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: () => api.cancelActivity(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['activities'] });
      qc.invalidateQueries({ queryKey: ['activity', id] });
    },
  });
}

export function useCompleteActivity(id: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: () => api.completeActivity(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['activities'] });
      qc.invalidateQueries({ queryKey: ['activity', id] });
    },
  });
}
