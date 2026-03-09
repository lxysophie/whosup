import {
  Card,
  CardActionArea,
  CardContent,
  Typography,
  Box,
  LinearProgress,
} from '@mui/material';
import { LocationOn, CalendarMonth, Person } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import type { Activity } from '../../types/activity';
import CategoryChip from './CategoryChip';
import StatusBadge from './StatusBadge';
import { formatDate } from '../../utils/date';

interface Props {
  activity: Activity;
}

export default function ActivityCard({ activity }: Props) {
  const navigate = useNavigate();
  const progress = (activity.currentParticipants / activity.maxParticipants) * 100;

  return (
    <Card>
      <CardActionArea onClick={() => navigate(`/activities/${activity.id}`)}>
        <CardContent>
          <Box display="flex" justifyContent="space-between" alignItems="center" mb={1}>
            <CategoryChip category={activity.category} />
            <StatusBadge status={activity.status} />
          </Box>

          <Typography variant="h6" gutterBottom noWrap>
            {activity.title}
          </Typography>

          <Box display="flex" alignItems="center" gap={0.5} mb={0.5} color="text.secondary">
            <LocationOn fontSize="small" />
            <Typography variant="body2" noWrap>
              {activity.location}
            </Typography>
          </Box>

          <Box display="flex" alignItems="center" gap={0.5} mb={0.5} color="text.secondary">
            <CalendarMonth fontSize="small" />
            <Typography variant="body2">{formatDate(activity.activityDate)}</Typography>
          </Box>

          <Box display="flex" alignItems="center" gap={0.5} mb={1.5} color="text.secondary">
            <Person fontSize="small" />
            <Typography variant="body2">by {activity.creator.displayName}</Typography>
          </Box>

          <Box>
            <Box display="flex" justifyContent="space-between" mb={0.5}>
              <Typography variant="caption" color="text.secondary">
                Participants
              </Typography>
              <Typography variant="caption" fontWeight={600}>
                {activity.currentParticipants}/{activity.maxParticipants}
              </Typography>
            </Box>
            <LinearProgress
              variant="determinate"
              value={progress}
              sx={{ borderRadius: 1, height: 6 }}
            />
          </Box>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}
