"use client";
import SendIcon from '@mui/icons-material/Send';
import { Box, IconButton, Tab, Tabs, TextField } from '@mui/material';
import { useState } from 'react';
import { useServices } from '../providers/ServiceProvider';
import PositionReportForm, { PositionReportModel } from './PositionReport/PositionReportForm';

export const PacketBuilder = () => {
  const { apiClient } = useServices();

  const [deviceName, setDeviceName] = useState('');
  const [sourceCallsign, setSourceCallsign] = useState('');
  const [activeTab, setActiveTab] = useState(0);
  const [positionReport, setPositionReport] = useState<PositionReportModel>({ latitude: '', longitude: '' });

  const handleNewDeviceName = (event: any) => {
    setDeviceName(event.target.value);
  };

  const handleNewSourceCallsign = (event: any) => {
    setSourceCallsign(event.target.value);
  };

  const handleTabChange = (event: React.ChangeEvent<{}>, newValue: number) => {
    setActiveTab(newValue);
  };

  const handleSend = () => {
    // Implement the send functionality here
    console.log('Send button clicked');
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
      <div>
        <Tabs value={activeTab} onChange={handleTabChange} aria-label="packet type tabs">
          <Tab label="Position Report" />
          <Tab label="Status" />
          <Tab label="Message" />
          <Tab label="Object" />
          <Tab label="Item" />
        </Tabs>
        <TabPanel value={activeTab} index={0}>
          <PositionReportForm onChange={setPositionReport} />
        </TabPanel>
        <TabPanel value={activeTab} index={1}>
          {/* Status fields */}
          <TextField label="Status Message" variant="outlined" fullWidth size="small" />
        </TabPanel>
        <TabPanel value={activeTab} index={2}>
          {/* Message fields */}
          <TextField label="Recipient" variant="outlined" fullWidth size="small" />
          <TextField label="Message" variant="outlined" fullWidth size="small" />
        </TabPanel>
        <TabPanel value={activeTab} index={3}>
          {/* Object fields */}
          <TextField label="Object Name" variant="outlined" fullWidth size="small" />
          <TextField label="Object Description" variant="outlined" fullWidth size="small" />
        </TabPanel>
        <TabPanel value={activeTab} index={4}>
          {/* Item fields */}
          <TextField label="Item Name" variant="outlined" fullWidth size="small" />
          <TextField label="Item Description" variant="outlined" fullWidth size="small" />
        </TabPanel>
      </div>
      <div style={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', gap: '16px' }}>
        <TextField
          color="secondary"
          label="Source callsign"
          variant="outlined"
          value={sourceCallsign}
          onChange={handleNewSourceCallsign}
          size="small"
        />
        <TextField
          color="secondary"
          label="Device name"
          variant="outlined"
          value={deviceName}
          onChange={handleNewDeviceName}
          size="small"
        />
        <IconButton color="secondary" onClick={handleSend}>
          <SendIcon />
        </IconButton>
      </div>
    </div>
  );
};

interface TabPanelProps {
  children?: React.ReactNode;
  index: any;
  value: any;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`tabpanel-${index}`}
      aria-labelledby={`tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box p={3}>
          {children}
        </Box>
      )}
    </div>
  );
}