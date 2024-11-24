"use client";

import { Button, TextField } from '@mui/material';
import { useEffect, useState } from 'react';

const Config = () => {
  const [loading, setLoading] = useState(true);
  const [config, setConfig] = useState({});

  useEffect(() => {
    fetchConfig();
  }, []);

  const fetchConfig = async () => {
    setLoading(true);
    const baseUrl = `${window.location.protocol}//${window.location.hostname}:8080`;
    const response = await fetch(`${baseUrl}/api/config`);
    const data = await response.json();
    setConfig(data);
    setLoading(false);
  };

  const handleReset = () => {
    fetchConfig();
  };

  const handleSave = async () => {
    console.log('Save');
  };

  const handleApply = async () => {
    console.log('Apply');
  };

  if (loading) {
    return <div>Loading...</div>;
  }
  return (
    <div>
      <TextField
        label="Configuration"
        multiline
        value={JSON.stringify(config, null, 2)}
        variant="outlined"
        fullWidth
        rows={20}
        InputProps={{
          readOnly: true,
        }}
      />
      <div style={{ marginTop: '10px' }}>
        <Button variant="contained" color="primary" onClick={handleReset}>
          Reset
        </Button>
        <Button variant="contained" color="primary" onClick={handleSave} style={{ marginLeft: '10px' }}>
          Save
        </Button>
        <Button variant="contained" color="secondary" onClick={handleApply} style={{ marginLeft: '10px' }}>
          Apply
        </Button>
      </div>
    </div>
  );
};

export default Config;