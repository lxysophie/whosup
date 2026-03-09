import { z } from 'zod';

export const activitySchema = z.object({
  title: z.string().min(3, 'Title must be at least 3 characters').max(200),
  description: z.string().max(2000).optional().or(z.literal('')),
  location: z.string().min(1, 'Location is required').max(300),
  activityDate: z.string().refine(
    (d) => new Date(d) > new Date(),
    { message: 'Activity date must be in the future' }
  ),
  category: z.enum([
    'SPORTS',
    'MOVIES',
    'DINING',
    'OUTDOORS',
    'GAMING',
    'STUDY',
    'MUSIC',
    'OTHER',
  ]),
  maxParticipants: z
    .number()
    .int()
    .min(2, 'Must have at least 2 participants')
    .max(100, 'Maximum 100 participants'),
});

export type ActivityFormData = z.infer<typeof activitySchema>;
