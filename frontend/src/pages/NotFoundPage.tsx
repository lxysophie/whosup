import { Box, Button, Typography } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

export default function NotFoundPage() {
  return (
    <Box textAlign="center" mt={8}>
      <Typography variant="h2" fontWeight={700} mb={2}>
        404
      </Typography>
      <Typography variant="h6" color="text.secondary" mb={3}>
        Page not found
      </Typography>
      <Button variant="contained" component={RouterLink} to="/">
        Go Home
      </Button>
    </Box>
  );
}
