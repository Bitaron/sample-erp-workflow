import { useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from '@mui/material';
import { toast } from 'react-toastify';
import { authApi, departmentApi, queryKeys, roleApi, userApi } from '../api/services';
import { getErrorMessage } from '../api/axios';
import { useAuth } from '../context/AuthContext';
import type { CreateUserRequest } from '../types';

export function UserManagementPage() {
  const { isAdmin } = useAuth();
  const queryClient = useQueryClient();
  const [createOpen, setCreateOpen] = useState(false);

  const { register, handleSubmit, reset, control, formState: { errors } } = useForm<CreateUserRequest>();

  const { data: users = [] } = useQuery({
    queryKey: queryKeys.users,
    queryFn: async () => {
      try {
        const response = await userApi.getList();
        return response.data;
      } catch (error) {
        toast.error(getErrorMessage(error, 'Access denied'));
        throw error;
      }
    },
    enabled: isAdmin,
  });

  const { data: departments = [] } = useQuery({
    queryKey: queryKeys.departments,
    queryFn: async () => {
      const response = await departmentApi.getList();
      return response.data;
    },
    enabled: createOpen,
  });

  const { data: roles = [] } = useQuery({
    queryKey: queryKeys.roles,
    queryFn: async () => {
      const response = await roleApi.getList();
      return response.data;
    },
    enabled: createOpen,
  });

  const createMutation = useMutation({
    mutationFn: (data: CreateUserRequest) => authApi.register(data),
    onSuccess: () => {
      toast.success('User created successfully');
      setCreateOpen(false);
      reset();
      queryClient.invalidateQueries({ queryKey: queryKeys.users });
    },
    onError: (error) => {
      toast.error(getErrorMessage(error, 'Failed to create user'));
    },
  });

  if (!isAdmin) {
    return (
      <Typography variant="h6" color="error">
        Access denied
      </Typography>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h5">User Management</Typography>
        <Button variant="contained" onClick={() => setCreateOpen(true)}>
          Create User
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Username</TableCell>
              <TableCell>Department</TableCell>
              <TableCell>Role</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.id}>
                <TableCell>{user.name}</TableCell>
                <TableCell>{user.userName}</TableCell>
                <TableCell>{user.department}</TableCell>
                <TableCell>{user.role}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={createOpen} onClose={() => setCreateOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Create User</DialogTitle>
        <Box component="form" onSubmit={handleSubmit((data) => createMutation.mutate(data))}>
          <DialogContent>
            <TextField
              label="Name"
              fullWidth
              margin="normal"
              {...register('name', { required: 'Name is required' })}
              error={!!errors.name}
              helperText={errors.name?.message}
            />
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
            <Controller
              name="departmentId"
              control={control}
              rules={{ required: 'Department is required' }}
              render={({ field }) => (
                <FormControl fullWidth margin="normal" error={!!errors.departmentId}>
                  <InputLabel>Department</InputLabel>
                  <Select {...field} label="Department" value={field.value ?? ''}>
                    {departments.map((dept) => (
                      <MenuItem key={dept.id} value={dept.id}>
                        {dept.name}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              )}
            />
            <Controller
              name="roleId"
              control={control}
              rules={{ required: 'Role is required' }}
              render={({ field }) => (
                <FormControl fullWidth margin="normal" error={!!errors.roleId}>
                  <InputLabel>Role</InputLabel>
                  <Select {...field} label="Role" value={field.value ?? ''}>
                    {roles.map((role) => (
                      <MenuItem key={role.id} value={role.id}>
                        {role.name}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              )}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setCreateOpen(false)}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={createMutation.isPending}>
              Create
            </Button>
          </DialogActions>
        </Box>
      </Dialog>
    </Box>
  );
}
