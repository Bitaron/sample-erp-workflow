import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AuthProvider } from './context/AuthContext';
import { AppLayout } from './components/layout/AppLayout';
import { LoadingOverlay } from './components/common/LoadingOverlay';
import { ProtectedRoute } from './routes/ProtectedRoute';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { BillingDocumentsPage } from './pages/BillingDocumentsPage';
import { WorkflowManagementPage } from './pages/WorkflowManagementPage';
import { DepartmentManagementPage } from './pages/DepartmentManagementPage';
import { UserManagementPage } from './pages/UserManagementPage';

const theme = createTheme();

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: false,
    },
  },
});

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
          <BrowserRouter>
            <LoadingOverlay />
            <Routes>
              <Route path="/login" element={<LoginPage />} />
              <Route element={<ProtectedRoute />}>
                <Route element={<AppLayout />}>
                  <Route path="/dashboard" element={<DashboardPage />} />
                  <Route path="/billing-documents" element={<BillingDocumentsPage />} />
                  <Route path="/workflows" element={<WorkflowManagementPage />} />
                  <Route path="/departments" element={<DepartmentManagementPage />} />
                  <Route path="/users" element={<UserManagementPage />} />
                </Route>
              </Route>
              <Route path="*" element={<Navigate to="/dashboard" replace />} />
            </Routes>
          </BrowserRouter>
          <ToastContainer position="top-right" autoClose={4000} />
        </AuthProvider>
      </ThemeProvider>
    </QueryClientProvider>
  );
}
