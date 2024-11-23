"use client"

import { CssBaseline } from '@mui/material';
import type { ReactNode } from "react";
import WebSocketProvider from "./PacketStreamProvider";
import { StoreProvider } from "./StoreProvider";

import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import { Nav } from './components/Nav';
import "./styles/globals.css";

interface Props {
  readonly children: ReactNode;
}

import { createTheme, ThemeProvider } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#CFD8DC',
    },
    secondary: {
      main: '#FFC107',
    },
  },
});

export default function RootLayout({ children }: Props) {
  return (
    <StoreProvider>
      <WebSocketProvider />
      <CssBaseline />
      <html lang="en">
        <body>
          <ThemeProvider theme={theme}>
            <Nav />
            <div style={{ padding: '16px' }}>
              {children}
            </div>
          </ThemeProvider>
        </body>
      </html>
    </StoreProvider>
  );
}