import { Paper, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import ActivityForm from '../components/activity/ActivityForm';
import ErrorAlert from '../components/common/ErrorAlert';
import { useCreateActivity } from '../hooks/useActivities';
import type { ActivityFormData } from '../validation/activitySchema';

export default function CreateActivityPage() {
  const navigate = useNavigate();
  const mutation = useCreateActivity();

  const handleSubmit = async (data: ActivityFormData) => {
    const result = await mutation.mutateAsync({
      ...data,
      activityDate: new Date(data.activityDate).toISOString(),
    });
    navigate(`/activities/${result.id}`);
  };

  return (
    <Paper sx={{ p: 4, maxWidth: 600, mx: 'auto' }}>
      <Typography variant="h5" fontWeight={700} mb={3}>
        Create Activity
      </Typography>
      {mutation.error && <ErrorAlert error={mutation.error} />}
      <ActivityForm onSubmit={handleSubmit} isSubmitting={mutation.isPending} />
    </Paper>
  );
}
