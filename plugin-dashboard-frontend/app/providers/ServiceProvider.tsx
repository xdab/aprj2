"use client";

import useApiClient, { ApiClient } from '@/lib/apiClient';
import { createContext, ReactNode, useContext } from 'react';

interface ServiceContextProps {
  apiClient: ApiClient;
}

const ServiceContext = createContext<ServiceContextProps | undefined>(undefined);

export const ServiceProvider = ({ children }: { children: ReactNode }) => {
  const apiClient = useApiClient();

  return (
    <ServiceContext.Provider value={{ apiClient }}>
      {children}
    </ServiceContext.Provider>
  );
};

export const useServices = (): ServiceContextProps => {
  const context = useContext(ServiceContext);
  if (!context) {
    throw new Error('useServices must be used within a ServiceProvider');
  }
  return context;
};