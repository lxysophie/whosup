import { Chip } from '@mui/material';
import type { ActivityCategory } from '../../types/activity';
import { CATEGORY_COLORS, CATEGORY_LABELS } from '../../utils/constants';

interface Props {
  category: ActivityCategory;
  size?: 'small' | 'medium';
}

export default function CategoryChip({ category, size = 'small' }: Props) {
  return (
    <Chip
      label={CATEGORY_LABELS[category]}
      size={size}
      sx={{
        bgcolor: CATEGORY_COLORS[category],
        color: 'white',
        fontWeight: 600,
      }}
    />
  );
}
