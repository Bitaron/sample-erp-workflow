export interface CurrentUser {
  id: number;
  name: string;
  department: string;
  role: string;
}

export interface LoginResponse {
  token: string;
}

export interface DashboardSummary {
  totalInProgress: number;
  totalCompleted: number;
  myPendingActions: number;
  myCompletedActions: number;
}

export interface BillingDocument {
  id: number;
  customerId: number;
  amount: number;
  createdBy: string;
  createdTime: string;
  status: string;
  invoiceNumber: string | null;
  invoiceId?: number | null;
  workflowInstanceId: number | null;
  canApprove: boolean;
}

export interface BillingDetail {
  id: number;
  customerId: number;
  amount: number;
  status: string;
  createdBy: string;
  createdTime: string;
  workflowStatus: string;
}

export interface WorkflowAuditEntry {
  userName: string;
  action: string;
  actionTime: string;
}

export interface WorkflowDefinition {
  id: number;
  name: string;
  documentType: string;
  createdBy: string;
  createdTime: string;
  steps?: WorkflowStep[];
}

export interface WorkflowStep {
  id?: number;
  sequence: number;
  departmentName: string;
}

export interface Department {
  id: number;
  name: string;
}

export interface Role {
  id: number;
  name: string;
}

export interface User {
  id: number;
  name: string;
  userName: string;
  department: string;
  role: string;
}

export interface InvoiceDetail {
  id: number;
  billingRequestId: number;
  invoiceNo: string;
  createdAt: string;
}

export interface CreateBillingRequest {
  customerId: number;
  amount: number;
}

export interface CreateWorkflowRequest {
  name: string;
  documentType: string;
  steps: { departmentId: number; sequenceOrder: number }[];
}

export interface CreateDepartmentRequest {
  name: string;
}

export interface CreateUserRequest {
  name: string;
  departmentId: number;
  roleId: number;
  userName: string;
  password: string;
}

export interface ApiError {
  message?: string;
  error?: string;
  timestamp?: string;
}
