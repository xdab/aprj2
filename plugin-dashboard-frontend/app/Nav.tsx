"use client";

import { AppBar, Box, Button, Toolbar, Typography } from '@mui/material';
import Link from "next/link";
import ConnectionIndicator from './Connection';

export const Nav = () => {
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component={Link} href="/" >
          aprj2 dashboard
        </Typography>
        <Box sx={{ mx: 2 }}>
          <ConnectionIndicator />
        </Box>
        <Box sx={{ flexGrow: 1 }} />
        <Button color="inherit" component={Link} href="/packetstream" sx={{ textTransform: 'none' }}>
          Packet stream
        </Button>
        <Button color="inherit" component={Link} href="/packetbuilder" sx={{ textTransform: 'none' }}>
          Packet builder
        </Button>
        <Button color="inherit" component={Link} href="/config" sx={{ textTransform: 'none' }}>
          Config
        </Button>
      </Toolbar>
    </AppBar>
  );
};