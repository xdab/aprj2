"use client";

import { RootState } from '@/lib/store';
import { useSelector } from 'react-redux';

export const Packets = () => {
  const packets = useSelector((state: RootState) => state.packets.packets);

  return (
    <div className="w-full">
      <div className="max-h-96 overflow-y-auto p-4">
        <table className="w-full border-collapse">
          <thead>
            <tr>
              <th className="bg-gray-200 font-bold text-left p-2">Direction</th>
              <th className="bg-gray-200 font-bold text-left p-2">Device</th>
              <th className="bg-gray-200 font-bold text-left p-2">Raw</th>
            </tr>
          </thead>
          <tbody>
            {packets.map((packet, index) => (
              <tr key={index}>
                <td className="border p-2">{packet.direction}</td>
                <td className="border p-2">{packet.device}</td>
                <td className="border p-2">{packet.rawPacket}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};