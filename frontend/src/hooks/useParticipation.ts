import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import * as api from '../api/activities';

export function useJoinActivity(id: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: () => api.joinActivity(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['activity', id] });
      qc.invalidateQueries({ queryKey: ['activities'] });
      qc.invalidateQueries({ queryKey: ['my-activities'] });
    },
  });
}

export function useLeaveActivity(id: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: () => api.leaveActivity(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['activity', id] });
      qc.invalidateQueries({ queryKey: ['activities'] });
      qc.invalidateQueries({ queryKey: ['my-activities'] });
    },
  });
}

export function useMyJoinedActivities() {
  return useQuery({
    queryKey: ['my-activities', 'joined'],
    queryFn: api.getMyJoinedActivities,
  });
}

export function useMyCreatedActivities() {
  return useQuery({
    queryKey: ['my-activities', 'created'],
    queryFn: api.getMyCreatedActivities,
  });
}
