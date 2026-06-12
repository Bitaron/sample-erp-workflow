import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  Box,
  Button,
  Collapse,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  Link,
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
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import { toast } from 'react-toastify';
import {
  billingApi,
  invoiceApi,
  queryKeys,
  workflowInstanceApi,
} from '../api/services';
import { getErrorMessage } from '../api/axios';
import type { BillingDetail, BillingDocument, InvoiceDetail } from '../types';

interface CreateBillingForm {
  customerId: number;
  amount: number;
}

function AuditLogRow({ workflowInstanceId }: { workflowInstanceId: number }) {
  const { data: auditLog = [] } = useQuery({
    queryKey: queryKeys.workflowAudit(workflowInstanceId),
    queryFn: async () => {
      try {
        const response = await workflowInstanceApi.getAudit(workflowInstanceId);
        return response.data;
      } catch (error) {
        toast.error(getErrorMessage(error, 'Failed to load workflow history'));
        throw error;
      }
    },
  });

  return (
    <Table size="small">
      <TableHead>
        <TableRow>
          <TableCell>Username</TableCell>
          <TableCell>Action</TableCell>
          <TableCell>Action Time</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {auditLog.map((entry, index) => (
          <TableRow key={`${entry.userName}-${entry.actionTime}-${index}`}>
            <TableCell>{entry.userName}</TableCell>
            <TableCell>{entry.action}</TableCell>
            <TableCell>{entry.actionTime}</TableCell>
          </TableRow>
        ))}
        {auditLog.length === 0 && (
          <TableRow>
            <TableCell colSpan={3} align="center">
              No audit entries
            </TableCell>
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
}

function BillingRow({
  row,
  onBillingClick,
  onInvoiceClick,
  onApprove,
  approving,
}: {
  row: BillingDocument;
  onBillingClick: (id: number) => void;
  onInvoiceClick: (invoiceId: number, invoiceNumber: string) => void;
  onApprove: (workflowInstanceId: number) => void;
  approving: boolean;
}) {
  const [open, setOpen] = useState(false);

  return (
    <>
      <TableRow hover>
        <TableCell>
          <IconButton size="small" onClick={() => setOpen(!open)} disabled={!row.workflowInstanceId}>
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell>
          <Link component="button" variant="body2" onClick={() => onBillingClick(row.id)}>
            {row.id}
          </Link>
        </TableCell>
        <TableCell>{row.customerId}</TableCell>
        <TableCell>{row.amount}</TableCell>
        <TableCell>{row.createdBy}</TableCell>
        <TableCell>{row.createdTime}</TableCell>
        <TableCell>{row.status}</TableCell>
        <TableCell>
          {row.invoiceNumber ? (
            <Link
              component="button"
              variant="body2"
              onClick={() =>
                onInvoiceClick(row.invoiceId ?? row.id, row.invoiceNumber as string)
              }
            >
              {row.invoiceNumber}
            </Link>
          ) : (
            '-'
          )}
        </TableCell>
        <TableCell>
          {row.canApprove && row.workflowInstanceId ? (
            <Button
              size="small"
              variant="contained"
              disabled={approving}
              onClick={() => onApprove(row.workflowInstanceId as number)}
            >
              Approve
            </Button>
          ) : null}
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell colSpan={9} sx={{ py: 0 }}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ m: 2 }}>
              <Typography variant="subtitle2" gutterBottom>
                Workflow Audit Log
              </Typography>
              {row.workflowInstanceId && <AuditLogRow workflowInstanceId={row.workflowInstanceId} />}
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
}

export function BillingDocumentsPage() {
  const queryClient = useQueryClient();
  const [createOpen, setCreateOpen] = useState(false);
  const [detailOpen, setDetailOpen] = useState(false);
  const [invoiceOpen, setInvoiceOpen] = useState(false);
  const [selectedBilling, setSelectedBilling] = useState<BillingDetail | null>(null);
  const [selectedInvoice, setSelectedInvoice] = useState<InvoiceDetail | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CreateBillingForm>();

  const { data: documents = [] } = useQuery({
    queryKey: queryKeys.billingDocuments,
    queryFn: async () => {
      try {
        const response = await billingApi.getList();
        const sorted = [...response.data].sort(
          (a, b) => new Date(b.createdTime).getTime() - new Date(a.createdTime).getTime(),
        );
        return sorted;
      } catch (error) {
        toast.error(getErrorMessage(error, 'Failed to load billing documents'));
        throw error;
      }
    },
  });

  const createMutation = useMutation({
    mutationFn: (data: CreateBillingForm) => billingApi.create(data),
    onSuccess: () => {
      toast.success('Billing request created successfully');
      setCreateOpen(false);
      reset();
      queryClient.invalidateQueries({ queryKey: queryKeys.billingDocuments });
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard });
    },
    onError: (error) => {
      toast.error(getErrorMessage(error, 'Please provide valid billing information'));
    },
  });

  const approveMutation = useMutation({
    mutationFn: (workflowInstanceId: number) => workflowInstanceApi.approve(workflowInstanceId),
    onSuccess: () => {
      toast.success('Approval successful');
      queryClient.invalidateQueries({ queryKey: queryKeys.billingDocuments });
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard });
    },
    onError: (error) => {
      toast.error(getErrorMessage(error, 'You are not authorized to approve this workflow'));
    },
  });

  const handleBillingClick = async (id: number) => {
    try {
      const response = await billingApi.getById(id);
      setSelectedBilling(response.data);
      setDetailOpen(true);
    } catch (error) {
      toast.error(getErrorMessage(error, 'Failed to load billing details'));
    }
  };

  const handleInvoiceClick = async (invoiceId: number, invoiceNumber: string) => {
    try {
      const response = await invoiceApi.getById(invoiceId);
      setSelectedInvoice(response.data);
      setInvoiceOpen(true);
    } catch {
      setSelectedInvoice({
        id: invoiceId,
        billingRequestId: 0,
        invoiceNo: invoiceNumber,
        createdAt: '-',
      });
      setInvoiceOpen(true);
      toast.error('Failed to load invoice details');
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h5">Billing Documents</Typography>
        <Button variant="contained" onClick={() => setCreateOpen(true)}>
          Create Billing Document
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell />
              <TableCell>Billing ID</TableCell>
              <TableCell>Customer ID</TableCell>
              <TableCell>Amount</TableCell>
              <TableCell>Created By</TableCell>
              <TableCell>Created Time</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Invoice Number</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {documents.map((row) => (
              <BillingRow
                key={row.id}
                row={row}
                onBillingClick={handleBillingClick}
                onInvoiceClick={handleInvoiceClick}
                onApprove={(id) => approveMutation.mutate(id)}
                approving={approveMutation.isPending}
              />
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={createOpen} onClose={() => setCreateOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Create Billing Document</DialogTitle>
        <Box component="form" onSubmit={handleSubmit((data) => createMutation.mutate(data))}>
          <DialogContent>
            <TextField
              label="Customer ID"
              type="number"
              fullWidth
              margin="normal"
              {...register('customerId', {
                required: 'Customer ID is required',
                valueAsNumber: true,
              })}
              error={!!errors.customerId}
              helperText={errors.customerId?.message}
            />
            <TextField
              label="Amount"
              type="number"
              fullWidth
              margin="normal"
              {...register('amount', {
                required: 'Amount is required',
                valueAsNumber: true,
                min: { value: 0.01, message: 'Amount must be greater than 0' },
              })}
              error={!!errors.amount}
              helperText={errors.amount?.message}
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

      <Dialog open={detailOpen} onClose={() => setDetailOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Billing Detail</DialogTitle>
        <DialogContent>
          {selectedBilling && (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, mt: 1 }}>
              <Typography>Billing ID: {selectedBilling.id}</Typography>
              <Typography>Customer ID: {selectedBilling.customerId}</Typography>
              <Typography>Amount: {selectedBilling.amount}</Typography>
              <Typography>Status: {selectedBilling.status}</Typography>
              <Typography>Created By: {selectedBilling.createdBy}</Typography>
              <Typography>Created Time: {selectedBilling.createdTime}</Typography>
              <Typography>Workflow Status: {selectedBilling.workflowStatus}</Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDetailOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={invoiceOpen} onClose={() => setInvoiceOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Invoice Detail</DialogTitle>
        <DialogContent>
          {selectedInvoice && (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, mt: 1 }}>
              <Typography>Invoice Number: {selectedInvoice.invoiceNo}</Typography>
              <Typography>Billing Request ID: {selectedInvoice.billingRequestId}</Typography>
              <Typography>Created At: {selectedInvoice.createdAt}</Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setInvoiceOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
