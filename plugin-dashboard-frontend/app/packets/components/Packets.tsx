"use client";

import { RootState } from '@/lib/store';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow
} from '@mui/material';
import { useSelector } from 'react-redux';

export const Packets = () => {
  const packets = useSelector((state: RootState) => state.packets.packets);

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Direction</TableCell>
            <TableCell>Device</TableCell>
            <TableCell>Raw</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {packets.map((packet, index) => (
            <TableRow key={index}>
              <TableCell>{packet.direction}</TableCell>
              <TableCell>{packet.device}</TableCell>
              <TableCell>{packet.rawPacket}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}