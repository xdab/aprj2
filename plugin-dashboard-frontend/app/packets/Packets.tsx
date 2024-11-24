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
            <TableCell>Timestamp</TableCell>
            <TableCell>Direction</TableCell>
            <TableCell>Device</TableCell>
            <TableCell>Source</TableCell>
            <TableCell>Destination</TableCell>
            <TableCell>Path</TableCell>
            <TableCell>Info</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {packets.map((packet, index) => (
            <TableRow key={index}>
              <TableCell>{packet.timestamp}</TableCell>
              <TableCell>{packet.direction}</TableCell>
              <TableCell>{packet.device}</TableCell>
              <TableCell>{packet.source.simple}</TableCell>
              <TableCell>{packet.destination.simple}</TableCell>
              <TableCell>{packet.path.map(callsign => callsign.full).join(",")}</TableCell>
              <TableCell>{packet.info}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}