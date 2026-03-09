import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Chip,
  Divider,
  Paper,
  Typography,
} from '@mui/material';
import {
  LocationOn,
  CalendarMonth,
  Person,
  Group,
} from '@mui/icons-material';
import { useActivity, useCancelActivity, useCompleteActivity, useDeleteActivity } from '../hooks/useActivities';
import { useJoinActivity, useLeaveActivity } from '../hooks/useParticipation';
import { useAuth } from '../context/AuthContext';
import CategoryChip from '../components/activity/CategoryChip';
import StatusBadge from '../components/activity/StatusBadge';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorAlert from '../components/common/ErrorAlert';
import ConfirmDialog from '../components/common/ConfirmDialog';
import { formatDate } from '../utils/date';

export default function ActivityDetailPage() {
  const { id } = useParams<{ id: string }>();
  const activityId = Number(id);
  const navigate = useNavigate();
  const { user } = useAuth();

  const { data: activity, isLoading, error } = useActivity(activityId);
  const joinMutation = useJoinActivity(activityId);
  const leaveMutation = useLeaveActivity(activityId);
  const cancelMutation = useCancelActivity(activityId);
  const completeMutation = useCompleteActivity(activityId);
  const deleteMutation = useDeleteActivity();

  const [confirmAction, setConfirmAction] = useState<'cancel' | 'delete' | null>(null);

  if (isLoading) return <LoadingSpinner />;
  if (error) return <ErrorAlert error={error} />;
  if (!activity) return null;

  const isCreator = user?.id === activity.creator.id;
  const isParticipant = activity.participants.some((p) => p.userId === user?.id);
  const canJoin =
    user && !isCreator && !isParticipant && activity.status === 'OPEN';
  const canLeave = user && isParticipant;
  const isActive = activity.status === 'OPEN' || activity.status === 'FULL';

  const handleConfirm = async () => {
    if (confirmAction === 'cancel') {
      await cancelMutation.mutateAsync();
    } else if (confirmAction === 'delete') {
      await deleteMutation.mutateAsync(activityId);
      navigate('/');
    }
    setConfirmAction(null);
  };

  return (
    <Paper sx={{ p: 4, maxWidth: 700, mx: 'auto' }}>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        <CategoryChip category={activity.category} size="medium" />
        <StatusBadge status={activity.status} />
      </Box>

      <Typography variant="h4" fontWeight={700} mb={2}>
        {activity.title}
      </Typography>

      <Box display="flex" flexDirection="column" gap={1} mb={3}>
        <Box display="flex" alignItems="center" gap={1} color="text.secondary">
          <LocationOn />
          <Typography>{activity.location}</Typography>
        </Box>
        <Box display="flex" alignItems="center" gap={1} color="text.secondary">
          <CalendarMonth />
          <Typography>{formatDate(activity.activityDate)}</Typography>
        </Box>
        <Box display="flex" alignItems="center" gap={1} color="text.secondary">
          <Person />
          <Typography>Created by {activity.creator.displayName}</Typography>
        </Box>
        <Box display="flex" alignItems="center" gap={1} color="text.secondary">
          <Group />
          <Typography>
            {activity.currentParticipants}/{activity.maxParticipants} participants
          </Typography>
        </Box>
      </Box>

      {activity.description && (
        <>
          <Divider sx={{ my: 2 }} />
          <Typography variant="body1" whiteSpace="pre-wrap">
            {activity.description}
          </Typography>
        </>
      )}

      <Divider sx={{ my: 2 }} />

      <Typography variant="h6" mb={1}>
        Participants
      </Typography>
      <Box display="flex" flexWrap="wrap" gap={1} mb={2}>
        <Chip label={activity.creator.displayName} color="primary" variant="outlined" size="small" />
        {activity.participants.map((p) => (
          <Chip key={p.userId} label={p.displayName} size="small" />
        ))}
      </Box>

      {/* Action buttons */}
      <Box display="flex" gap={2} mt={3} flexWrap="wrap">
        {canJoin && (
          <Button
            variant="contained"
            onClick={() => joinMutation.mutate()}
            disabled={joinMutation.isPending}
          >
            {joinMutation.isPending ? 'Joining...' : 'Join Activity'}
          </Button>
        )}

        {canLeave && (
          <Button
            variant="outlined"
            color="warning"
            onClick={() => leaveMutation.mutate()}
            disabled={leaveMutation.isPending}
          >
            {leaveMutation.isPending ? 'Leaving...' : 'Leave Activity'}
          </Button>
        )}

        {user && !isCreator && !isParticipant && activity.status === 'FULL' && (
          <Button variant="contained" disabled>
            Activity Full
          </Button>
        )}

        {isCreator && isActive && (
          <>
            <Button
              variant="outlined"
              onClick={() => navigate(`/activities/${activityId}/edit`)}
            >
              Edit
            </Button>
            <Button
              variant="outlined"
              color="success"
              onClick={() => completeMutation.mutate()}
              disabled={completeMutation.isPending}
            >
              Mark Completed
            </Button>
            <Button
              variant="outlined"
              color="error"
              onClick={() => setConfirmAction('cancel')}
            >
              Cancel Activity
            </Button>
          </>
        )}

        {isCreator && (
          <Button
            variant="outlined"
            color="error"
            onClick={() => setConfirmAction('delete')}
          >
            Delete
          </Button>
        )}
      </Box>

      {(joinMutation.error || leaveMutation.error) && (
        <Box mt={2}>
          <ErrorAlert error={joinMutation.error || leaveMutation.error} />
        </Box>
      )}

      <ConfirmDialog
        open={confirmAction !== null}
        title={confirmAction === 'cancel' ? 'Cancel Activity' : 'Delete Activity'}
        message={
          confirmAction === 'cancel'
            ? 'Are you sure you want to cancel this activity? This cannot be undone.'
            : 'Are you sure you want to delete this activity? This cannot be undone.'
        }
        onConfirm={handleConfirm}
        onCancel={() => setConfirmAction(null)}
      />
    </Paper>
  );
}
