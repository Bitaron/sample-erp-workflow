import { createContext, useContext, useMemo, type ReactNode } from 'react';
import { useQuery } from '@tanstack/react-query';
import { authApi, queryKeys } from '../api/services';
import { clearToken, getToken } from '../api/axios';
import type { CurrentUser } from '../types';

interface AuthContextValue {
  user: CurrentUser | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  isAdmin: boolean;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const hasToken = !!getToken();

  const { data: user, isLoading } = useQuery({
    queryKey: queryKeys.currentUser,
    queryFn: async () => {
      const response = await authApi.getCurrentUser();
      return response.data;
    },
    enabled: hasToken,
    retry: false,
  });

  const value = useMemo<AuthContextValue>(
    () => {
      // 1. If we have a token and React Query is still fetching, we are in a "loading" state.
      const isAuthLoading = hasToken && isLoading;
  
      return {
        user: user ?? null,
        isLoading: isAuthLoading,
        // 2. CRITICAL: If we are still loading the user profile, do NOT declare them unauthenticated yet.
        isAuthenticated: isAuthLoading ? false : !!user,
        isAdmin: user?.role === 'ADMIN',
        logout: () => {
          clearToken();
          window.location.href = '/login';
        },
      };
    },
    [user, hasToken, isLoading],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}
