import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';

const ChatAppPage = () => {
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  let client = null;

  useEffect(() => {
    // Підключення до WebSocket через STOMP
    client = new Client({
      brokerURL: 'ws://localhost:8080/test/chat',  // Ваш WebSocket ендпоінт
      //reconnectDelay: 5000,
      onConnect: () => {
        console.log('Connected to WebSocket');

        // Підписка на повідомлення від сервера
        client.subscribe('/topic/messages', (message) => {
          if (message.body) {
            setMessages(prevMessages => [...prevMessages, message.body]);
          }
        });
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    client.activate();

    return () => {
      // Закриття з'єднання при закритті компонента
      if (client) {
        client.deactivate();
      }
    };
  }, []);

  const sendMessage = () => {
    if (client && inputMessage.trim()) {
      client.publish({ destination: '/test/chat', body: inputMessage });
      setInputMessage('');  // Очищення поля введення після відправки
    }
  };

  return (
    <div>
      <h2>WebSocket Chat</h2>
      <div className="messages">
        {messages.map((message, index) => (
          <div key={index}>{message}</div>
        ))}
      </div>
      <input
        type="text"
        value={inputMessage}
        onChange={(e) => setInputMessage(e.target.value)}
        placeholder="Type a message..."
      />
      <button onClick={sendMessage}>Send</button>
    </div>
  );
};

export default ChatAppPage;
