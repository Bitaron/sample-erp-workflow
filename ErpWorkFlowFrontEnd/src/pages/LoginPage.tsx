import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Navigate, useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  TextField,
  Typography,
} from '@mui/material';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-toastify';
import { authApi, queryKeys } from '../api/services';
import { getErrorMessage, getToken, setToken } from '../api/axios';
import { useAuth } from '../context/AuthContext';

interface LoginForm {
  userName: string;
  password: string;
}

export function LoginPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { isAuthenticated, isLoading } = useAuth();

  if (getToken() && (isLoading || isAuthenticated)) {
    return isAuthenticated ? <Navigate to="/dashboard" replace /> : null;
  }
  const { register, handleSubmit, formState: { errors } } = useForm<LoginForm>();
  const [submitting, setSubmitting] = useState(false);

  const loginMutation = useMutation({
    mutationFn: async (data: LoginForm) => {
      const loginRes = await authApi.login(data.userName, data.password);
      setToken(loginRes.data.token);
      await authApi.getCurrentUser();
      return loginRes.data;
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: queryKeys.currentUser });
      navigate('/dashboard');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error, 'Invalid username or password'));
    },
  });

  const onSubmit = async (data: LoginForm) => {
    setSubmitting(true);
    try {
      await loginMutation.mutateAsync(data);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        bgcolor: 'grey.100',
      }}
    >
      <Card sx={{ width: 400 }}>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h5" gutterBottom align="center">
            ERP Workflow Login
          </Typography>
          <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ mt: 2 }}>
            <TextField
              label="Username"
              fullWidth
              margin="normal"
              {...register('userName', { required: 'Username is required' })}
              error={!!errors.userName}
              helperText={errors.userName?.message}
            />
            <TextField
              label="Password"
              type="password"
              fullWidth
              margin="normal"
              {...register('password', { required: 'Password is required' })}
              error={!!errors.password}
              helperText={errors.password?.message}
            />
            <Button
              type="submit"
              variant="contained"
              fullWidth
              sx={{ mt: 2 }}
              disabled={submitting || loginMutation.isPending}
            >
              {submitting || loginMutation.isPending ? <CircularProgress size={24} /> : 'Login'}
            </Button>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
}
