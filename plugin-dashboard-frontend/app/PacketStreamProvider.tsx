"use client"

import { addPacket, Packet } from '@/lib/features/packets/packetsSlice';
import { Client } from '@stomp/stompjs';
import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import SockJS from 'sockjs-client';

const WebSocketProvider: React.FC = () => {
  const dispatch = useDispatch();

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
          const packet: Packet = JSON.parse(message.body);
          console.log(packet);
          dispatch(addPacket(packet));
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
  }, [dispatch]);

  return null;
};

export default WebSocketProvider;