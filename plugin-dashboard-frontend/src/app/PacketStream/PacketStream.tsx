import { Client } from '@stomp/stompjs';
import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { PacketMessage } from './PacketMessage';

const PacketStream: React.FC = () => {
  const [messages, setMessages] = useState<PacketMessage[]>([]);

  useEffect(() => {
    const protocol = window.location.protocol;
    const host = window.location.hostname;
    const wsUrl = `${protocol}//${host}:8080/ws`;

    console.log('Connecting to ' + wsUrl);

    const socket = new SockJS(wsUrl);
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log('Connected');
        client.subscribe('/topic/packets', (message) => {
          const packetMessage: PacketMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, packetMessage]);
        });
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <div className="w-full">
      <div className="max-h-96 overflow-y-auto p-4">
        <table className="w-full border-collapse">
          <thead>
            <tr>
              <th className="bg-gray-200 font-bold text-left p-2 w-2/10">Direction</th>
              <th className="bg-gray-200 font-bold text-left p-2 w-2/10">Device</th>
              <th className="bg-gray-200 font-bold text-left p-2 w-6/10">Packet</th>
            </tr>
          </thead>
          <tbody>
            {messages.length === 0 ? (
              <tr>
                <td className="p-2 border w-2/10">-</td>
                <td className="p-2 border w-2/10">-</td>
                <td className="p-2 border w-6/10">-</td>
              </tr>
            ) : (
              messages.map((message, index) => (
                <tr key={index} className={index % 2 === 0 ? 'bg-gray-100' : ''}>
                  <td className="p-2 border w-2/10">{message.direction}</td>
                  <td className="p-2 border w-2/10">{message.device}</td>
                  <td className="p-2 border w-6/10">{message.packetString}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default PacketStream;