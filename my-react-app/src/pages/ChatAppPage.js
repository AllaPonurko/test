import React, { useState, useEffect } from 'react';

const WebSocketComponent = () => {
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  const [ws, setWs] = useState(null);

  useEffect(() => {
    const socket = new WebSocket('ws://localhost:8082/test/init');

    socket.onopen = () => {
      console.log('WebSocket connected');
    };
    socket.onclose = (event) => {
      console.log('WebSocket closed. Reconnecting...');
      //setTimeout(() => connectWebSocket(), 5000); // Повторне підключення через 5 секунд
    };


    socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    // Обробка повідомлень від сервера
    socket.onmessage = (event) => {
      console.log('Received:', event.data);
      setMessages((prevMessages) => [...prevMessages, event.data]);
    };
    setWs(socket);

    // Закриття з'єднання при закритті компонента
    return () => {
      if (ws) {
        ws.close();
        console.log('WebSocket disconnected');
      }
    };
  }, []);

  const sendMessage = () => {
    if (ws && ws.readyState === WebSocket.OPEN && inputMessage.trim()) {
      ws.send(inputMessage);
      console.log('Sent:', inputMessage);
      setInputMessage('');
    } else {
      console.error('WebSocket is not open');
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

export default WebSocketComponent;
