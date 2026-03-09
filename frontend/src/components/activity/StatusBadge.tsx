import { Chip } from '@mui/material';
import type { ActivityStatus } from '../../types/activity';

const STATUS_CONFIG: Record<ActivityStatus, { label: string; color: 'success' | 'error' | 'warning' | 'default' }> = {
  OPEN: { label: 'Open', color: 'success' },
  FULL: { label: 'Full', color: 'warning' },
  CANCELLED: { label: 'Cancelled', color: 'error' },
  COMPLETED: { label: 'Completed', color: 'default' },
};

interface Props {
  status: ActivityStatus;
}

export default function StatusBadge({ status }: Props) {
  const config = STATUS_CONFIG[status];
  return <Chip label={config.label} color={config.color} size="small" variant="outlined" />;
}
