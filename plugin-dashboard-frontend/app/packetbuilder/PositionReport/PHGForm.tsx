import { Box, FormControl, InputLabel, MenuItem, Select, Typography } from '@mui/material';
import React from 'react';

export enum PHGPower {
  W_1 = '1W',
  W_4 = '4W',
  W_9 = '9W',
  W_16 = '16W',
  W_25 = '25W',
  W_36 = '36W',
  W_49 = '49W',
  W_64 = '64W',
  W_81 = '81W',
  W_100 = '100W'
}

export enum PHGHeight {
  Ft_10 = '10ft',
  Ft_20 = '20ft',
  Ft_40 = '40ft',
  Ft_80 = '80ft',
  Ft_160 = '160ft',
  Ft_320 = '320ft',
  Ft_640 = '640ft',
  Ft_1280 = '1280ft',
  Ft_2560 = '2560ft',
  Ft_5120 = '5120ft'
}

export enum PHGGain {
  DBi_0 = '0dBi',
  DBi_1 = '1dBi',
  DBi_2 = '2dBi',
  DBi_3 = '3dBi',
  DBi_4 = '4dBi',
  DBi_5 = '5dBi',
  DBi_6 = '6dBi',
  DBi_7 = '7dBi',
  DBi_8 = '8dBi',
  DBi_9 = '9dBi'
}

export enum PHGDirectivity {
  Omni = 'Omni',
  NE = 'NE',
  E = 'E',
  SE = 'SE',
  S = 'S',
  SW = 'SW',
  W = 'W',
  NW = 'NW',
  N = 'N'
}

interface PHGFormProps {
  phg: {
    power: PHGPower;
    height: PHGHeight;
    gain: PHGGain;
    directivity?: PHGDirectivity;
  };
  onChange: (phg: { power: PHGPower; height: PHGHeight; gain: PHGGain; directivity?: PHGDirectivity }) => void;
}

const PHGForm: React.FC<PHGFormProps> = ({ phg, onChange }) => {
  const handlePhgChange = (event: React.ChangeEvent<{ value: unknown }>, type: 'power' | 'height' | 'gain' | 'directivity') => {
    const newPhg = { ...phg, [type]: event.target.value as string };
    onChange(newPhg);
  };

  return (
    <Box display="flex" flexDirection="row" alignItems="center" gap="16px" mt={2}>
      <Typography variant="body1" style={{ width: '10%' }}>
        PHG
      </Typography>
      <FormControl variant="outlined" size="small" style={{ flex: 1 }}>
        <InputLabel>Power</InputLabel>
        <Select
          value={phg.power}
          onChange={(e) => handlePhgChange(e, 'power')}
          label="Power"
        >
          {Object.values(PHGPower).map((value) => (
            <MenuItem key={value} value={value}>
              {value}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl variant="outlined" size="small" style={{ flex: 1 }}>
        <InputLabel>Height</InputLabel>
        <Select
          value={phg.height}
          onChange={(e) => handlePhgChange(e, 'height')}
          label="Height"
        >
          {Object.values(PHGHeight).map((value) => (
            <MenuItem key={value} value={value}>
              {value}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl variant="outlined" size="small" style={{ flex: 1 }}>
        <InputLabel>Gain</InputLabel>
        <Select
          value={phg.gain}
          onChange={(e) => handlePhgChange(e, 'gain')}
          label="Gain"
        >
          {Object.values(PHGGain).map((value) => (
            <MenuItem key={value} value={value}>
              {value}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl variant="outlined" size="small" style={{ flex: 1 }}>
        <InputLabel>Directivity</InputLabel>
        <Select
          value={phg.directivity}
          onChange={(e) => handlePhgChange(e, 'directivity')}
          label="Directivity"
        >
          {Object.values(PHGDirectivity).map((value) => (
            <MenuItem key={value} value={value}>
              {value}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Box>
  );
};

export default PHGForm;