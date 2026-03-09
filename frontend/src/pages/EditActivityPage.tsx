import { Paper, Typography } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';
import ActivityForm from '../components/activity/ActivityForm';
import ErrorAlert from '../components/common/ErrorAlert';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { useActivity, useUpdateActivity } from '../hooks/useActivities';
import type { ActivityFormData } from '../validation/activitySchema';

export default function EditActivityPage() {
  const { id } = useParams<{ id: string }>();
  const activityId = Number(id);
  const navigate = useNavigate();

  const { data: activity, isLoading, error } = useActivity(activityId);
  const mutation = useUpdateActivity(activityId);

  if (isLoading) return <LoadingSpinner />;
  if (error) return <ErrorAlert error={error} />;
  if (!activity) return null;

  const handleSubmit = async (data: ActivityFormData) => {
    await mutation.mutateAsync({
      ...data,
      activityDate: new Date(data.activityDate).toISOString(),
    });
    navigate(`/activities/${activityId}`);
  };

  // Convert ISO date to datetime-local format
  const localDate = new Date(activity.activityDate);
  const dateStr = localDate.toISOString().slice(0, 16);

  return (
    <Paper sx={{ p: 4, maxWidth: 600, mx: 'auto' }}>
      <Typography variant="h5" fontWeight={700} mb={3}>
        Edit Activity
      </Typography>
      {mutation.error && <ErrorAlert error={mutation.error} />}
      <ActivityForm
        defaultValues={{
          title: activity.title,
          description: activity.description || '',
          location: activity.location,
          activityDate: dateStr,
          category: activity.category,
          maxParticipants: activity.maxParticipants,
        }}
        onSubmit={handleSubmit}
        isSubmitting={mutation.isPending}
        submitLabel="Save Changes"
      />
    </Paper>
  );
}
