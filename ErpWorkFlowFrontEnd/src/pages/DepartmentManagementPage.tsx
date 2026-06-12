import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Paper,
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
import { departmentApi, queryKeys } from '../api/services';
import { getErrorMessage } from '../api/axios';
import { useAuth } from '../context/AuthContext';

interface CreateDepartmentForm {
  name: string;
}

export function DepartmentManagementPage() {
  const { isAdmin } = useAuth();
  const queryClient = useQueryClient();
  const [createOpen, setCreateOpen] = useState(false);

  const { register, handleSubmit, reset, formState: { errors } } = useForm<CreateDepartmentForm>();

  const { data: departments = [] } = useQuery({
    queryKey: queryKeys.departments,
    queryFn: async () => {
      const response = await departmentApi.getList();
      return response.data;
    },
  });

  const createMutation = useMutation({
    mutationFn: (data: CreateDepartmentForm) => departmentApi.create(data),
    onSuccess: () => {
      toast.success('Department created successfully');
      setCreateOpen(false);
      reset();
      queryClient.invalidateQueries({ queryKey: queryKeys.departments });
    },
    onError: (error) => {
      toast.error(getErrorMessage(error, 'Failed to create department'));
    },
  });

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h5">Department Management</Typography>
        {isAdmin && (
          <Button variant="contained" onClick={() => setCreateOpen(true)}>
            Create Department
          </Button>
        )}
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Department ID</TableCell>
              <TableCell>Department Name</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {departments.map((dept) => (
              <TableRow key={dept.id}>
                <TableCell>{dept.id}</TableCell>
                <TableCell>{dept.name}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={createOpen} onClose={() => setCreateOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Create Department</DialogTitle>
        <Box component="form" onSubmit={handleSubmit((data) => createMutation.mutate(data))}>
          <DialogContent>
            <TextField
              label="Department Name"
              fullWidth
              margin="normal"
              {...register('name', { required: 'Department name is required' })}
              error={!!errors.name}
              helperText={errors.name?.message}
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
