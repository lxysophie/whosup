import { useState } from 'react';
import { Grid, Tab, Tabs, Typography } from '@mui/material';
import { useMyCreatedActivities, useMyJoinedActivities } from '../hooks/useParticipation';
import ActivityCard from '../components/activity/ActivityCard';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorAlert from '../components/common/ErrorAlert';

export default function MyActivitiesPage() {
  const [tab, setTab] = useState(0);
  const created = useMyCreatedActivities();
  const joined = useMyJoinedActivities();

  const { data, isLoading, error } = tab === 0 ? created : joined;

  return (
    <>
      <Typography variant="h4" fontWeight={700} mb={3}>
        My Activities
      </Typography>

      <Tabs value={tab} onChange={(_, v) => setTab(v)} sx={{ mb: 3 }}>
        <Tab label="Created" />
        <Tab label="Joined" />
      </Tabs>

      {isLoading && <LoadingSpinner />}
      {error && <ErrorAlert error={error} />}

      {data && data.length === 0 && (
        <Typography color="text.secondary" textAlign="center" mt={4}>
          {tab === 0
            ? "You haven't created any activities yet."
            : "You haven't joined any activities yet."}
        </Typography>
      )}

      {data && data.length > 0 && (
        <Grid container spacing={3}>
          {data.map((activity) => (
            <Grid size={{ xs: 12, sm: 6, md: 4 }} key={activity.id}>
              <ActivityCard activity={activity} />
            </Grid>
          ))}
        </Grid>
      )}
    </>
  );
}
