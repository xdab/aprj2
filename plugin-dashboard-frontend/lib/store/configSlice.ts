import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export interface Aprj2Config {

}

export interface ConfigState {
  aprj2Config?: Aprj2Config;
}

const initialState: ConfigState = {
  aprj2Config: undefined,
};

const configSlice = createSlice({
  name: 'config',
  initialState,
  reducers: {
    setConfig: (state, action: PayloadAction<Aprj2Config>) => {
      state.aprj2Config = action.payload;
    },
  },
});
export const { setConfig } = configSlice.actions;
export default configSlice.reducer;