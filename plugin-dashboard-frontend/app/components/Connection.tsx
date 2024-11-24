import { RootState } from '@/lib/store';
import { Box } from '@mui/material';
import { useSelector } from 'react-redux';

const green = '#00FF00';
const red = '#FF0000';

const ConnectionIndicator = () => {
  const isWsConnected = useSelector((state: RootState) => state.connection.isWsConnected);

  return (
    <Box
      width={12}
      height={12}
      bgcolor={isWsConnected ? green : red}
      borderRadius="50%"
      marginRight={1}
      border="1px solid black"
    />
  );
};

export default ConnectionIndicator;