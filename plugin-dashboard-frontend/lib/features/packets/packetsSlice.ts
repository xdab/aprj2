import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export interface Packet {
  direction: string;
  device: string;
  rawPacket: string;
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