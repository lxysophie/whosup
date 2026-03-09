import {
  Box,
  Button,
  MenuItem,
  TextField,
} from '@mui/material';
import { useForm, Controller, type SubmitHandler } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { activitySchema, type ActivityFormData } from '../../validation/activitySchema';
import { CATEGORY_LABELS } from '../../utils/constants';
import type { ActivityCategory } from '../../types/activity';

interface Props {
  defaultValues?: Partial<ActivityFormData>;
  onSubmit: SubmitHandler<ActivityFormData>;
  isSubmitting?: boolean;
  submitLabel?: string;
}

const categories = Object.entries(CATEGORY_LABELS) as [ActivityCategory, string][];

export default function ActivityForm({
  defaultValues,
  onSubmit,
  isSubmitting = false,
  submitLabel = 'Create Activity',
}: Props) {
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<ActivityFormData>({
    resolver: zodResolver(activitySchema),
    defaultValues: {
      title: '',
      description: '',
      location: '',
      activityDate: '',
      category: 'OTHER',
      maxParticipants: 4,
      ...defaultValues,
    },
  });

  return (
    <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
      <Controller
        name="title"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            label="Title"
            fullWidth
            margin="normal"
            error={!!errors.title}
            helperText={errors.title?.message}
          />
        )}
      />

      <Controller
        name="description"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            label="Description"
            fullWidth
            margin="normal"
            multiline
            rows={3}
            error={!!errors.description}
            helperText={errors.description?.message}
          />
        )}
      />

      <Controller
        name="location"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            label="Location"
            fullWidth
            margin="normal"
            error={!!errors.location}
            helperText={errors.location?.message}
          />
        )}
      />

      <Controller
        name="activityDate"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            label="Date & Time"
            type="datetime-local"
            fullWidth
            margin="normal"
            slotProps={{ inputLabel: { shrink: true } }}
            error={!!errors.activityDate}
            helperText={errors.activityDate?.message}
          />
        )}
      />

      <Controller
        name="category"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            select
            label="Category"
            fullWidth
            margin="normal"
            error={!!errors.category}
            helperText={errors.category?.message}
          >
            {categories.map(([value, label]) => (
              <MenuItem key={value} value={value}>
                {label}
              </MenuItem>
            ))}
          </TextField>
        )}
      />

      <Controller
        name="maxParticipants"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            label="Max Participants"
            type="number"
            fullWidth
            margin="normal"
            slotProps={{ htmlInput: { min: 2, max: 100 } }}
            error={!!errors.maxParticipants}
            helperText={errors.maxParticipants?.message}
          />
        )}
      />

      <Button
        type="submit"
        variant="contained"
        size="large"
        fullWidth
        disabled={isSubmitting}
        sx={{ mt: 2 }}
      >
        {isSubmitting ? 'Saving...' : submitLabel}
      </Button>
    </Box>
  );
}
