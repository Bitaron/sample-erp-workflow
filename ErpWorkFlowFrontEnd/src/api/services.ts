import { api } from './axios';
import type {
  BillingDetail,
  BillingDocument,
  CreateBillingRequest,
  CreateDepartmentRequest,
  CreateUserRequest,
  CreateWorkflowRequest,
  CurrentUser,
  DashboardSummary,
  Department,
  InvoiceDetail,
  LoginResponse,
  Role,
  User,
  WorkflowAuditEntry,
  WorkflowDefinition,
} from '../types';

export const authApi = {
  login: (userName: string, password: string) =>
    api.post<LoginResponse>('/auth/login', { userName, password }),
  getCurrentUser: () => api.get<CurrentUser>('/auth/me'),
  register: (data: CreateUserRequest) => api.post('/auth/register', data),
};

export const dashboardApi = {
  getSummary: () => api.get<DashboardSummary>('/dashboard/summary'),
};

export const billingApi = {
  getList: (page = 0, size = 20) =>
    api.get<BillingDocument[]>('/bill-requests', { params: { page, size } }),
  getById: (id: number) => api.get<BillingDetail>(`/bill-requests/${id}`),
  create: (data: CreateBillingRequest) => api.post('/bill-requests', data),
};

export const workflowInstanceApi = {
  approve: (id: number) => api.post(`/workflow-instances/${id}/approve`, {}),
  getAudit: (id: number) => api.get<WorkflowAuditEntry[]>(`/workflow-instances/${id}/audit`),
};

export const workflowApi = {
  getList: () => api.get<WorkflowDefinition[]>('/workflows'),
  getById: (id: number) => api.get<WorkflowDefinition>(`/workflows/${id}`),
  create: async (data: CreateWorkflowRequest) => {
    const workflowRes = await api.post<WorkflowDefinition>('/workflows', {
      name: data.name,
      documentType: data.documentType,
    });
    const workflowId = workflowRes.data.id;
    for (const step of data.steps) {
      await api.post(`/workflows/${workflowId}/steps`, step);
    }
    return workflowRes;
  },
};

export const departmentApi = {
  getList: () => api.get<Department[]>('/departments'),
  create: (data: CreateDepartmentRequest) => api.post<Department>('/departments', data),
};

export const roleApi = {
  getList: () => api.get<Role[]>('/roles'),
};

export const userApi = {
  getList: () => api.get<User[]>('/users'),
};

export const invoiceApi = {
  getById: (id: number) => api.get<InvoiceDetail>(`/invoices/${id}`),
};

export const queryKeys = {
  currentUser: ['currentUser'] as const,
  dashboard: ['dashboard'] as const,
  billingDocuments: ['billingDocuments'] as const,
  billingDocument: (id: number) => ['billingDocument', id] as const,
  workflowAudit: (workflowInstanceId: number) => ['workflowAudit', workflowInstanceId] as const,
  workflows: ['workflows'] as const,
  workflow: (workflowId: number) => ['workflow', workflowId] as const,
  departments: ['departments'] as const,
  roles: ['roles'] as const,
  users: ['users'] as const,
  invoice: (invoiceId: number) => ['invoice', invoiceId] as const,
};
