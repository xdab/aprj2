"use client";

import { RootState } from '@/lib/store';
import CallMadeIcon from '@mui/icons-material/CallMade';
import CallReceivedIcon from '@mui/icons-material/CallReceived';
import SendIcon from '@mui/icons-material/Send';
import {
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField
} from '@mui/material';
import moment from 'moment';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import { useServices } from '../providers/ServiceProvider';

export const PacketStream = () => {
  const { apiClient } = useServices();

  var [rawPacket, setRawPacket] = useState('');
  var [deviceName, setDeviceName] = useState('');
  const packets = useSelector((state: RootState) => state.packets.packets);

  const handleNewDeviceName = (event: any) => {
    setDeviceName(event.target.value);
  }

  const handleNewPacketText = (event: any) => {
    setRawPacket(event.target.value);
  }

  const handleSendPacket = (event: any) => {
    const request = { raw: rawPacket, device: deviceName };
    apiClient.post("/api/packet", request);
  }

  return (
    <TableContainer component={Paper}>
      <div style={{ display: 'flex', alignItems: 'center', padding: '16px', columnGap: '8px' }}>
        <TextField color="secondary" variant="outlined" size="small"
          placeholder="Device name"
          value={deviceName}
          onChange={handleNewDeviceName} />
        <TextField color="secondary" variant="outlined" size="small" fullWidth
          placeholder="Packet in TNC2 format"
          value={rawPacket}
          onChange={handleNewPacketText} />
        <IconButton color="secondary" onClick={handleSendPacket}>
          <SendIcon />
        </IconButton>
      </div>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell></TableCell>
            <TableCell>Time</TableCell>
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
              <TableCell>{packet.direction == 'RX'
                ? (<CallReceivedIcon style={{ color: 'lime' }} />)
                : (<CallMadeIcon style={{ color: 'red' }} />)}
              </TableCell>
              <TableCell style={{ whiteSpace: 'pre-line' }}>
                {(moment(new Date(packet.timestamp))).format('HH:mm:ss')}
              </TableCell>
              <TableCell>{packet.device}</TableCell>
              <TableCell>{packet.source.simple}</TableCell>
              <TableCell>{packet.destination.simple}</TableCell>
              <TableCell>{packet.path.map(callsign => callsign.full).join(",")}</TableCell>
              <TableCell>{packet.info}</TableCell>
            </TableRow>
          )).reverse()}
        </TableBody>
      </Table>
    </TableContainer>
  );
}