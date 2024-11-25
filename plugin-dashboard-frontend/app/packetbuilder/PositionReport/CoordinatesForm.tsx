import { Box, TextField, Typography } from '@mui/material';
import React from 'react';

interface CoordinatesFormProps {
  latitude: number;
  longitude: number;
  onChange: (latitude: number, longitude: number) => void;
}

const CoordinatesForm: React.FC<CoordinatesFormProps> = ({ latitude, longitude, onChange }) => {
  const convertDecimalToDDM = (decimal: string) => {
    const decimalValue = parseFloat(decimal) || 0;
    const degrees = Math.floor(decimalValue);
    const minutes = ((decimalValue - degrees) * 60).toFixed(2);
    return { degrees: degrees.toString(), minutes: minutes.toString() };
  };

  const convertDDMToDecimal = (degrees: string, minutes: string) => {
    const degreesValue = parseFloat(degrees) || 0;
    const minutesValue = parseFloat(minutes) || 0;
    const decimalDegrees = degreesValue + minutesValue / 60;
    return decimalDegrees.toFixed(5).toString();
  };

  const [latitudeDecimal, setLatitudeDecimal] = React.useState(latitude.toString());
  const [longitudeDecimal, setLongitudeDecimal] = React.useState(longitude.toString());
  const [latitudeDDM, setLatitudeDDM] = React.useState(convertDecimalToDDM(latitude.toString()));
  const [longitudeDDM, setLongitudeDDM] = React.useState(convertDecimalToDDM(longitude.toString()));

  const handleLatitudeDecimalChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const newLatitudeDecimal = event.target.value;
    setLatitudeDecimal(newLatitudeDecimal);
    const { degrees, minutes } = convertDecimalToDDM(newLatitudeDecimal);
    setLatitudeDDM({ degrees, minutes });
    onChange(parseFloat(newLatitudeDecimal), parseFloat(longitudeDecimal));
  };

  const handleLongitudeDecimalChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const newLongitudeDecimal = event.target.value;
    setLongitudeDecimal(newLongitudeDecimal);
    const { degrees, minutes } = convertDecimalToDDM(newLongitudeDecimal);
    setLongitudeDDM({ degrees, minutes });
    onChange(parseFloat(latitudeDecimal), parseFloat(newLongitudeDecimal));
  };

  const handleLatitudeDDMChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, type: 'degrees' | 'minutes') => {
    const newLatitudeDDM = { ...latitudeDDM, [type]: event.target.value };
    setLatitudeDDM(newLatitudeDDM);
    const newLatitudeDecimal = convertDDMToDecimal(newLatitudeDDM.degrees, newLatitudeDDM.minutes);
    setLatitudeDecimal(newLatitudeDecimal);
    onChange(parseFloat(newLatitudeDecimal), parseFloat(longitudeDecimal));
  };

  const handleLongitudeDDMChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, type: 'degrees' | 'minutes') => {
    const newLongitudeDDM = { ...longitudeDDM, [type]: event.target.value };
    setLongitudeDDM(newLongitudeDDM);
    const newLongitudeDecimal = convertDDMToDecimal(newLongitudeDDM.degrees, newLongitudeDDM.minutes);
    setLongitudeDecimal(newLongitudeDecimal);
    onChange(parseFloat(latitudeDecimal), parseFloat(newLongitudeDecimal));
  };


  return (
    <Box>
      <Box display="flex" flexDirection="row" alignItems="center" gap="16px">
        <Typography variant="body1" style={{ width: '10%' }}>
          Latitude
        </Typography>
        <TextField
          label="Decimal degrees"
          variant="outlined"
          size="small"
          value={latitudeDecimal}
          onChange={handleLatitudeDecimalChange}
          inputProps={{ maxLength: 8, step: "0.00001" }}
          style={{ flex: 4 }}
        />
        <Typography variant="body1" style={{ margin: '0 8px' }}>
          or
        </Typography>
        <Box display="flex" flexDirection="row" gap="8px" style={{ flex: 6 }}>
          <TextField
            label="Degrees"
            variant="outlined"
            size="small"
            value={latitudeDDM.degrees}
            onChange={(e) => handleLatitudeDDMChange(e, 'degrees')}
            inputProps={{ maxLength: 2 }}
            style={{ flex: 2 }}
          />
          <TextField
            label="Decimal minutes"
            variant="outlined"
            size="small"
            value={latitudeDDM.minutes}
            onChange={(e) => handleLatitudeDDMChange(e, 'minutes')}
            inputProps={{ maxLength: 5, step: "0.01" }}
            style={{ flex: 3 }}
          />
        </Box>
      </Box>
      <Box display="flex" flexDirection="row" alignItems="center" gap="16px" mt={2}>
        <Typography variant="body1" style={{ width: '10%' }}>
          Longitude
        </Typography>
        <TextField
          label="Decimal degrees"
          variant="outlined"
          size="small"
          value={longitudeDecimal}
          onChange={handleLongitudeDecimalChange}
          inputProps={{ maxLength: 8, step: "0.00001" }}
          style={{ flex: 4 }}
        />
        <Typography variant="body1" style={{ margin: '0 8px' }}>
          or
        </Typography>
        <Box display="flex" flexDirection="row" gap="8px" style={{ flex: 6 }}>
          <TextField
            label="Degrees"
            variant="outlined"
            size="small"
            value={longitudeDDM.degrees}
            onChange={(e) => handleLongitudeDDMChange(e, 'degrees')}
            inputProps={{ maxLength: 2 }}
            style={{ flex: 2 }}
          />
          <TextField
            label="Decimal minutes"
            variant="outlined"
            size="small"
            value={longitudeDDM.minutes}
            onChange={(e) => handleLongitudeDDMChange(e, 'minutes')}
            inputProps={{ maxLength: 5, step: "0.01" }}
            style={{ flex: 3 }}
          />
        </Box>
      </Box>
    </Box>
  );
};

export default CoordinatesForm;