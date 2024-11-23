import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface ConnectionState {
  isWsConnected: boolean;
}

const initialState: ConnectionState = {
  isWsConnected: false,
};

const connectionSlice = createSlice({
  name: 'connection',
  initialState,
  reducers: {
    setWsConnected: (state, action: PayloadAction<boolean>) => {
      state.isWsConnected = action.payload;
    },
  },
});

export const { setWsConnected } = connectionSlice.actions;
export default connectionSlice.reducer;