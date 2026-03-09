import type { ActivityCategory } from '../types/activity';

export const CATEGORY_LABELS: Record<ActivityCategory, string> = {
  SPORTS: 'Sports',
  MOVIES: 'Movies',
  DINING: 'Dining',
  OUTDOORS: 'Outdoors',
  GAMING: 'Gaming',
  STUDY: 'Study',
  MUSIC: 'Music',
  OTHER: 'Other',
};

export const CATEGORY_COLORS: Record<ActivityCategory, string> = {
  SPORTS: '#4caf50',
  MOVIES: '#f44336',
  DINING: '#ff9800',
  OUTDOORS: '#2196f3',
  GAMING: '#9c27b0',
  STUDY: '#607d8b',
  MUSIC: '#e91e63',
  OTHER: '#795548',
};
