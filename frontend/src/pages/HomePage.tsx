import { useState } from 'react';
import {
  Box,
  Grid,
  Typography,
  TextField,
  MenuItem,
  InputAdornment,
  Pagination,
} from '@mui/material';
import { Search } from '@mui/icons-material';
import { useActivities } from '../hooks/useActivities';
import ActivityCard from '../components/activity/ActivityCard';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorAlert from '../components/common/ErrorAlert';
import { CATEGORY_LABELS } from '../utils/constants';
import type { ActivityCategory } from '../types/activity';

const categories = Object.entries(CATEGORY_LABELS) as [ActivityCategory, string][];

export default function HomePage() {
  const [page, setPage] = useState(0);
  const [category, setCategory] = useState('');
  const [search, setSearch] = useState('');

  const { data, isLoading, error } = useActivities({
    page,
    size: 12,
    status: 'OPEN',
    category: category || undefined,
    search: search || undefined,
  });

  return (
    <>
      <Typography variant="h4" fontWeight={700} mb={1}>
        Find Activities Near You
      </Typography>
      <Typography variant="body1" color="text.secondary" mb={3}>
        Browse activities and find partners for your next adventure
      </Typography>

      <Box display="flex" gap={2} mb={3} flexWrap="wrap">
        <TextField
          placeholder="Search activities..."
          size="small"
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setPage(0);
          }}
          sx={{ minWidth: 250 }}
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <Search />
                </InputAdornment>
              ),
            },
          }}
        />
        <TextField
          select
          size="small"
          value={category}
          onChange={(e) => {
            setCategory(e.target.value);
            setPage(0);
          }}
          sx={{ minWidth: 150 }}
          label="Category"
        >
          <MenuItem value="">All Categories</MenuItem>
          {categories.map(([value, label]) => (
            <MenuItem key={value} value={value}>
              {label}
            </MenuItem>
          ))}
        </TextField>
      </Box>

      {isLoading && <LoadingSpinner />}
      {error && <ErrorAlert error={error} />}

      {data && (
        <>
          {data.content.length === 0 ? (
            <Typography color="text.secondary" textAlign="center" mt={4}>
              No activities found. Try adjusting your filters or create one!
            </Typography>
          ) : (
            <Grid container spacing={3}>
              {data.content.map((activity) => (
                <Grid size={{ xs: 12, sm: 6, md: 4 }} key={activity.id}>
                  <ActivityCard activity={activity} />
                </Grid>
              ))}
            </Grid>
          )}

          {data.totalPages > 1 && (
            <Box display="flex" justifyContent="center" mt={4}>
              <Pagination
                count={data.totalPages}
                page={page + 1}
                onChange={(_, p) => setPage(p - 1)}
                color="primary"
              />
            </Box>
          )}
        </>
      )}
    </>
  );
}
