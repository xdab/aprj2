"use client"

import { setWsConnected } from '@/lib/store/connectionSlice';
import { addPacket, Packet } from '@/lib/store/packetsSlice';
import { Client } from '@stomp/stompjs';
import React, { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import SockJS from 'sockjs-client';

const WebSocketProvider: React.FC = () => {
  const dispatch = useDispatch();
  const [reconnectAttempts, setReconnectAttempts] = useState(0);

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
        dispatch(setWsConnected(true));
        setReconnectAttempts(0); // Reset reconnect attempts on successful connection
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
      onDisconnect: () => {
        console.log('Disconnected');
        dispatch(setWsConnected(false));
      },
      onWebSocketClose: () => {
        console.log('WebSocket closed');
        dispatch(setWsConnected(false));
        setTimeout(() => {
          console.log(`Reconnect attempt ${reconnectAttempts + 1}`);
          setReconnectAttempts(reconnectAttempts + 1);
          client.activate();
        }, 5000);
      },
      connectionTimeout: 2500,
      heartbeatOutgoing: 2500,
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, [dispatch, reconnectAttempts]);

  return null;
};

export default WebSocketProvider;