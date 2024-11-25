import { Box, Checkbox, FormControl, FormControlLabel, InputLabel, MenuItem, Select, TextField, Typography } from '@mui/material';
import React from 'react';

export enum QSYTone {
  Off = 'Off',
  Tone = 'Tone',
  ToneSquelch = 'Tone Squelch'
}

interface QSYFormProps {
  qsy: {
    frequency: string;
    shift: string;
    tone: QSYTone;
    narrow: boolean;
  };
  onChange: (qsy: { frequency: string; shift: string; tone: QSYTone; narrow: boolean }) => void;
}

const QSYForm: React.FC<QSYFormProps> = ({ qsy, onChange }) => {
  const handleQsyChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | React.ChangeEvent<{ value: unknown }>, type: 'frequency' | 'shift' | 'tone' | 'narrow') => {
    const newQsy = { ...qsy, [type]: type === 'narrow' ? (event.target as HTMLInputElement).checked : event.target.value as string };
    onChange(newQsy);
  };

  return (
    <Box display="flex" flexDirection="row" alignItems="center" gap="16px" mt={2}>
      <Typography variant="body1" style={{ width: '10%' }}>
        QSY
      </Typography>
      <TextField
        label="Frequency"
        variant="outlined"
        size="small"
        type="number"
        value={qsy.frequency}
        onChange={(e) => handleQsyChange(e, 'frequency')}
        inputProps={{ maxLength: 6 }}
        style={{ flex: 1 }}
      />
      <TextField
        label="Shift"
        variant="outlined"
        size="small"
        type="number"
        value={qsy.shift}
        onChange={(e) => handleQsyChange(e, 'shift')}
        inputProps={{ maxLength: 3 }}
        style={{ flex: 1 }}
      />
      <FormControl variant="outlined" size="small" style={{ flex: 1 }}>
        <InputLabel>Tone</InputLabel>
        <Select
          value={qsy.tone}
          onChange={(e) => handleQsyChange(e, 'tone')}
          label="Tone"
        >
          {Object.values(QSYTone).map((value) => (
            <MenuItem key={value} value={value}>
              {value}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControlLabel
        control={
          <Checkbox
            checked={qsy.narrow}
            onChange={(e) => handleQsyChange(e, 'narrow')}
            color="primary"
          />
        }
        label="Narrow FM"
        style={{ flex: 1 }}
      />
    </Box>
  );
};

export default QSYForm;