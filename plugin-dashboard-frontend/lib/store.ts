import { configureStore } from "@reduxjs/toolkit";
import connectionSlice from "./store/connectionSlice";
import packetsSlice from "./store/packetsSlice";

export const makeStore = () => {
  return configureStore({
    reducer: {
      packets: packetsSlice,
      connection: connectionSlice,
    },
  });
};

export type AppStore = ReturnType<typeof makeStore>;
export type RootState = ReturnType<AppStore['getState']>;
export type AppDispatch = AppStore['dispatch'];