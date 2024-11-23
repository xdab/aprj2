import { RootState } from '@/lib/store';
import { Box, Typography } from '@mui/material';
import { alpha } from '@mui/system';
import { useSelector } from 'react-redux';

const green = '#00FF00';
const red = '#FF0000';

const ConnectionStatus = () => {
  const isWsConnected = useSelector((state: RootState) => state.connection.isWsConnected);

  return (
    <Box
      display="flex"
      alignItems="center"
      sx={{
        marginRight: 2,
        px: 0.75,
        py: 0.25,
        border: 2,
        borderRadius: 2,
        borderColor: isWsConnected ? alpha(green, 0.5) : alpha(red, 0.5),
        bgcolor: isWsConnected ? alpha(green, 0.1) : alpha(red, 0.2),
      }}
    >
      <Box
        width={12}
        height={12}
        bgcolor={isWsConnected ? green : red}
        borderRadius="50%"
        marginRight={1}
      />
      <Typography variant="caption">{isWsConnected ? 'Connected' : 'Not connected'}</Typography>
    </Box>
  );
};

export default ConnectionStatus;