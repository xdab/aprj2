import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export interface Callsign {
  full: string;
  simple: string;
  base: string;
  ssid: number;
  repeated: boolean;
}

export interface Packet {
  timestamp: string;
  direction: string;
  device: string;
  source: Callsign;
  destination: Callsign;
  path: Callsign[];
  info: string;
}

export interface PacketState {
  packets: Packet[];
}

const initialState: PacketState = {
  packets: [],
};

const packetSlice = createSlice({
  name: 'packets',
  initialState,
  reducers: {
    addPacket: (state, action: PayloadAction<Packet>) => {
      state.packets.push(action.payload);
    },
    clearPackets: (state) => {
      state.packets = [];
    },
  },
});

export const { addPacket, clearPackets } = packetSlice.actions;
export default packetSlice.reducer;