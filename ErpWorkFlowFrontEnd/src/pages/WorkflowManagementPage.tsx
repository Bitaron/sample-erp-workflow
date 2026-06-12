import { useState } from 'react';
import { Controller, useFieldArray, useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  Box,
  Button,
  Collapse,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  IconButton,
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
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import { toast } from 'react-toastify';
import { departmentApi, queryKeys, workflowApi } from '../api/services';
import { getErrorMessage } from '../api/axios';
import { useAuth } from '../context/AuthContext';
import type { WorkflowDefinition } from '../types';

const DOCUMENT_TYPES = [{ value: 'BILL_REQUEST', label: 'Bill Request' }];

interface WorkflowStepForm {
  departmentId: number;
  sequenceOrder: number;
}

interface CreateWorkflowForm {
  name: string;
  documentType: string;
  steps: WorkflowStepForm[];
}

function WorkflowRow({ row }: { row: WorkflowDefinition }) {
  const [open, setOpen] = useState(false);

  const { data: detail } = useQuery({
    queryKey: queryKeys.workflow(row.id),
    queryFn: async () => {
      const response = await workflowApi.getById(row.id);
      return response.data;
    },
    enabled: open,
  });

  const steps = detail?.steps ?? [];

  return (
    <>
      <TableRow hover>
        <TableCell>
          <IconButton size="small" onClick={() => setOpen(!open)}>
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell>{row.name}</TableCell>
        <TableCell>{row.createdBy}</TableCell>
        <TableCell>{row.createdTime}</TableCell>
        <TableCell>{row.documentType}</TableCell>
      </TableRow>
      <TableRow>
        <TableCell colSpan={5} sx={{ py: 0 }}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ m: 2 }}>
              <Typography variant="subtitle2" gutterBottom>
                Workflow Steps
              </Typography>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell>Sequence</TableCell>
                    <TableCell>Department</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {steps.map((step) => (
                    <TableRow key={step.id ?? `${step.sequence}-${step.departmentName}`}>
                      <TableCell>{step.sequence}</TableCell>
                      <TableCell>{step.departmentName}</TableCell>
                    </TableRow>
                  ))}
                  {steps.length === 0 && (
                    <TableRow>
                      <TableCell colSpan={2} align="center">
                        No steps defined
                      </TableCell>
                    </TableRow>
                  )}
                </TableBody>
              </Table>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
}

export function WorkflowManagementPage() {
  const { isAdmin } = useAuth();
  const queryClient = useQueryClient();
  const [createOpen, setCreateOpen] = useState(false);
  const [step, setStep] = useState(1);

  const { data: workflows = [] } = useQuery({
    queryKey: queryKeys.workflows,
    queryFn: async () => {
      try {
        const response = await workflowApi.getList();
        return response.data;
      } catch (error) {
        toast.error(getErrorMessage(error, 'Failed to load workflows'));
        throw error;
      }
    },
  });

  const { data: departments = [] } = useQuery({
    queryKey: queryKeys.departments,
    queryFn: async () => {
      const response = await departmentApi.getList();
      return response.data;
    },
    enabled: createOpen,
  });

  const {
    register,
    handleSubmit,
    control,
    reset,
    watch,
    formState: { errors },
  } = useForm<CreateWorkflowForm>({
    defaultValues: {
      name: '',
      documentType: 'BILL_REQUEST',
      steps: [{ departmentId: 0, sequenceOrder: 1 }],
    },
  });

  const { fields, append, remove } = useFieldArray({ control, name: 'steps' });
  const workflowName = watch('name');
  const documentType = watch('documentType');

  const createMutation = useMutation({
    mutationFn: (data: CreateWorkflowForm) =>
      workflowApi.create({
        name: data.name,
        documentType: data.documentType,
        steps: data.steps.map((s, index) => ({
          departmentId: s.departmentId,
          sequenceOrder: s.sequenceOrder || index + 1,
        })),
      }),
    onSuccess: () => {
      toast.success('Workflow created successfully');
      setCreateOpen(false);
      setStep(1);
      reset();
      queryClient.invalidateQueries({ queryKey: queryKeys.workflows });
    },
    onError: (error) => {
      toast.error(getErrorMessage(error, 'Failed to create workflow'));
    },
  });

  const openCreateDialog = () => {
    reset({
      name: '',
      documentType: 'BILL_REQUEST',
      steps: [{ departmentId: departments[0]?.id ?? 0, sequenceOrder: 1 }],
    });
    setStep(1);
    setCreateOpen(true);
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h5">Workflow Management</Typography>
        {isAdmin && (
          <Button variant="contained" onClick={openCreateDialog}>
            Create Workflow
          </Button>
        )}
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell />
              <TableCell>Workflow Name</TableCell>
              <TableCell>Created By</TableCell>
              <TableCell>Created Time</TableCell>
              <TableCell>Document Type</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {workflows.map((row) => (
              <WorkflowRow key={row.id} row={row} />
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={createOpen} onClose={() => setCreateOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>Create Workflow</DialogTitle>
        <Box
          component="form"
          onSubmit={handleSubmit((data) => createMutation.mutate(data))}
        >
          <DialogContent>
            {step === 1 && (
              <Box>
                <TextField
                  label="Workflow Name"
                  fullWidth
                  margin="normal"
                  {...register('name', { required: 'Workflow name is required' })}
                  error={!!errors.name}
                  helperText={errors.name?.message}
                />
                <Controller
                  name="documentType"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <FormControl fullWidth margin="normal">
                      <InputLabel>Document Type</InputLabel>
                      <Select {...field} label="Document Type">
                        {DOCUMENT_TYPES.map((dt) => (
                          <MenuItem key={dt.value} value={dt.value}>
                            {dt.label}
                          </MenuItem>
                        ))}
                      </Select>
                    </FormControl>
                  )}
                />
              </Box>
            )}

            {step === 2 && (
              <Box>
                <Typography variant="subtitle1" gutterBottom>
                  Workflow Steps
                </Typography>
                {fields.map((field, index) => (
                  <Box key={field.id} sx={{ display: 'flex', gap: 2, mb: 2, alignItems: 'center' }}>
                    <Controller
                      name={`steps.${index}.departmentId`}
                      control={control}
                      rules={{ required: true }}
                      render={({ field: deptField }) => (
                        <FormControl sx={{ minWidth: 200 }}>
                          <InputLabel>Department</InputLabel>
                          <Select {...deptField} label="Department" value={deptField.value ?? ''}>
                            {departments.map((dept) => (
                              <MenuItem key={dept.id} value={dept.id}>
                                {dept.name}
                              </MenuItem>
                            ))}
                          </Select>
                        </FormControl>
                      )}
                    />
                    <TextField
                      label="Sequence"
                      type="number"
                      sx={{ width: 120 }}
                      {...register(`steps.${index}.sequenceOrder`, {
                        required: true,
                        valueAsNumber: true,
                      })}
                    />
                    <IconButton
                      onClick={() => remove(index)}
                      disabled={fields.length === 1}
                      color="error"
                    >
                      <DeleteIcon />
                    </IconButton>
                  </Box>
                ))}
                <Button
                  startIcon={<AddIcon />}
                  onClick={() =>
                    append({ departmentId: departments[0]?.id ?? 0, sequenceOrder: fields.length + 1 })
                  }
                >
                  Add Step
                </Button>
              </Box>
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setCreateOpen(false)}>Cancel</Button>
            {step === 1 && (
              <Button
                variant="contained"
                onClick={() => workflowName && setStep(2)}
                disabled={!workflowName}
              >
                Next
              </Button>
            )}
            {step === 2 && (
              <>
                <Button onClick={() => setStep(1)}>Back</Button>
                <Button type="submit" variant="contained" disabled={createMutation.isPending}>
                  Create
                </Button>
              </>
            )}
          </DialogActions>
        </Box>
      </Dialog>
    </Box>
  );
}
