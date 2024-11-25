"use client";

import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import { useState } from "react";

export interface ApiClient {
  get<T>(url: string, config?: AxiosRequestConfig): Promise<T>;
  post<T>(url: string, data: any, config?: AxiosRequestConfig): Promise<T>;
  setApiBaseURL(url: string): void;
  baseURL: string;
};

export const useApiClient = (): ApiClient => {
  const defaultBaseURL = `${window.location.protocol}//${window.location.hostname}:8080`;
  const [baseURL, setBaseURL] = useState<string>(defaultBaseURL);

  const instance = axios.create({
    baseURL,
  });

  const get = async<T>(url: string, config: AxiosRequestConfig = {}): Promise<T> => {
    const response: AxiosResponse<T> = await instance.get(url, config);
    return response.data;
  };

  const post = async <T>(url: string, data: any, config: AxiosRequestConfig = {}): Promise<T> => {
    const response: AxiosResponse<T> = await instance.post(url, data, config);
    return response.data;
  };

  const setApiBaseURL = (url: string): void => {
    setBaseURL(url);
  };

  return { get, post, setApiBaseURL, baseURL };
};

export default useApiClient;