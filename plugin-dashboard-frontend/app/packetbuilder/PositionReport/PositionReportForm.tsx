import { Box, Link, TextField, Typography } from '@mui/material';
import React from 'react';
import CoordinatesForm from './CoordinatesForm';
import PHGForm, { PHGDirectivity, PHGGain, PHGHeight, PHGPower } from './PHGForm';
import QSYForm, { QSYTone } from './QSYForm';

export interface PositionReportModel {
  latitude: number;
  longitude: number;
  symbol?: string;
  comment?: string;
  altitude?: number;
  phg?: {
    power: PHGPower;
    height: PHGHeight;
    gain: PHGGain;
    directivity?: PHGDirectivity;
  };
  course?: number;
  speed?: number;
  qsy?: {
    frequency: string;
    shift: string;
    tone?: QSYTone;
    narrow: boolean;
  };
}

interface PositionReportFormProps {
  onChange: (positionReport: PositionReportModel) => void;
}

const PositionReportForm: React.FC<PositionReportFormProps> = ({ onChange }) => {
  const [latitude, setLatitude] = React.useState(0);
  const [longitude, setLongitude] = React.useState(0);
  const [symbol, setSymbol] = React.useState('');
  const [comment, setComment] = React.useState('');
  const [altitude, setAltitude] = React.useState<number | ''>('');
  const [phg, setPhg] = React.useState({ power: PHGPower.W_1, height: PHGHeight.Ft_10, gain: PHGGain.DBi_0, directivity: PHGDirectivity.Omni });
  const [course, setCourse] = React.useState<number | ''>('');
  const [speed, setSpeed] = React.useState<number | ''>('');
  const [qsy, setQsy] = React.useState({ frequency: '', shift: '', tone: QSYTone.Off, narrow: false });

  const handleCoordinatesChange = (newLatitude: number, newLongitude: number) => {
    setLatitude(newLatitude);
    setLongitude(newLongitude);
    onChange({ latitude: newLatitude, longitude: newLongitude, symbol, comment, altitude: parseFloat(altitude as string), phg, course: parseFloat(course as string), speed: parseFloat(speed as string), qsy });
  };

  const handleSymbolChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const newSymbol = event.target.value;
    setSymbol(newSymbol);
    onChange({ latitude, longitude, symbol: newSymbol, comment, altitude: parseFloat(altitude as string), phg, course: parseFloat(course as string), speed: parseFloat(speed as string), qsy });
  };

  const handleCommentChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const newComment = event.target.value;
    setComment(newComment);
    onChange({ latitude, longitude, symbol, comment: newComment, altitude: parseFloat(altitude as string), phg, course: parseFloat(course as string), speed: parseFloat(speed as string), qsy });
  };

  const handleAltitudeChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const newAltitude = event.target.value;
    setAltitude(newAltitude === '' ? '' : parseFloat(newAltitude));
    onChange({ latitude, longitude, symbol, comment, altitude: newAltitude === '' ? undefined : parseFloat(newAltitude), phg, course: parseFloat(course as string), speed: parseFloat(speed as string), qsy });
  };

  const handlePhgChange = (newPhg: { power: PHGPower; height: PHGHeight; gain: PHGGain; directivity?: PHGDirectivity }) => {
    setPhg(newPhg);
    onChange({ latitude, longitude, symbol, comment, altitude: parseFloat(altitude as string), phg: newPhg, course: parseFloat(course as string), speed: parseFloat(speed as string), qsy });
  };

  const handleCourseChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const newCourse = event.target.value;
    setCourse(newCourse === '' ? '' : parseFloat(newCourse));
    onChange({ latitude, longitude, symbol, comment, altitude: parseFloat(altitude as string), phg, course: newCourse === '' ? undefined : parseFloat(newCourse), speed: parseFloat(speed as string), qsy });
  };

  const handleSpeedChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const newSpeed = event.target.value;
    setSpeed(newSpeed === '' ? '' : parseFloat(newSpeed));
    onChange({ latitude, longitude, symbol, comment, altitude: parseFloat(altitude as string), phg, course: parseFloat(course as string), speed: newSpeed === '' ? undefined : parseFloat(newSpeed), qsy });
  };

  const handleQsyChange = (newQsy: { frequency: string; shift: string; tone: QSYTone; narrow: boolean }) => {
    setQsy(newQsy);
    onChange({ latitude, longitude, symbol, comment, altitude: parseFloat(altitude as string), phg, course: parseFloat(course as string), speed: parseFloat(speed as string), qsy: newQsy });
  };

  return (
    <Box>
      <CoordinatesForm latitude={latitude} longitude={longitude} onChange={handleCoordinatesChange} />
      <Box display="flex" flexDirection="row" alignItems="center" gap="16px" mt={2}>
        <TextField
          label="Comment"
          variant="outlined"
          size="small"
          fullWidth
          value={comment}
          onChange={handleCommentChange}
          style={{ flex: 4 }}
        />
        <TextField
          label="Symbol"
          variant="outlined"
          size="small"
          value={symbol}
          onChange={handleSymbolChange}
          inputProps={{ maxLength: 2 }}
          style={{ flex: 1 }}
        />
        <Link href="https://www.yachttrack.org/info_camper/downloads/APRS_Symbol_Chart.pdf" target="_blank" rel="noopener" style={{ flex: 1 }}>
          Reference (PDF)
        </Link>
      </Box>
      <Box mt={2}>
        <TextField
          label="Altitude"
          variant="outlined"
          size="small"
          fullWidth
          type="number"
          value={altitude}
          onChange={handleAltitudeChange}
        />
      </Box>
      <PHGForm phg={phg} onChange={handlePhgChange} />
      <Box display="flex" flexDirection="row" alignItems="center" gap="16px" mt={2}>
        <Typography variant="body1" style={{ width: '10%' }}>
          Course & Speed
        </Typography>
        <TextField
          label="Course"
          variant="outlined"
          size="small"
          type="number"
          value={course}
          onChange={handleCourseChange}
          inputProps={{ maxLength: 3 }}
          style={{ flex: 1 }}
        />
        <TextField
          label="Speed"
          variant="outlined"
          size="small"
          type="number"
          value={speed}
          onChange={handleSpeedChange}
          inputProps={{ maxLength: 3 }}
          style={{ flex: 1 }}
        />
      </Box>
      <QSYForm qsy={qsy} onChange={handleQsyChange} />
    </Box>
  );
};

export default PositionReportForm;