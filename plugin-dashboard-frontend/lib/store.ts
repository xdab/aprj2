import { configureStore } from "@reduxjs/toolkit";
import configSlice from "./store/configSlice";
import connectionSlice from "./store/connectionSlice";
import packetsSlice from "./store/packetsSlice";

export const makeStore = () => {
  return configureStore({
    reducer: {
      packets: packetsSlice,
      connection: connectionSlice,
      config: configSlice,
    },
  });
};

export type AppStore = ReturnType<typeof makeStore>;
export type RootState = ReturnType<AppStore['getState']>;
export type AppDispatch = AppStore['dispatch'];