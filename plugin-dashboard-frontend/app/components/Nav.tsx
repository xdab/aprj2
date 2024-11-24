"use client";

import { AppBar, Box, Button, Toolbar, Typography } from '@mui/material';
import Link from "next/link";
import ConnectionStatus from './ConnectionStatus';

export const Nav = () => {
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component={Link} href="/" >
          aprj2 dashboard
        </Typography>
        <Box sx={{ mx: 2 }}>
          <ConnectionStatus />
        </Box>
        <Box sx={{ flexGrow: 1 }} />
        <Button color="inherit" component={Link} href="/packets" sx={{ textTransform: 'none' }}>
          Packets
        </Button>
        <Button color="inherit" component={Link} href="/verify" sx={{ textTransform: 'none' }}>
          Verify
        </Button>
        <Button color="inherit" component={Link} href="/config" sx={{ textTransform: 'none' }}>
          Config
        </Button>
      </Toolbar>
    </AppBar>
  );
};