import { Alert } from '@mui/material';
import { AxiosError } from 'axios';
import type { ErrorResponse } from '../../types/api';

interface Props {
  error: unknown;
}

export default function ErrorAlert({ error }: Props) {
  let message = 'An unexpected error occurred';

  if (error instanceof AxiosError && error.response?.data) {
    const data = error.response.data as ErrorResponse;
    message = data.message || message;
  } else if (error instanceof Error) {
    message = error.message;
  }

  return (
    <Alert severity="error" sx={{ mb: 2 }}>
      {message}
    </Alert>
  );
}
