import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const OrderProcessingPage = () => {
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);

  useEffect(() => {
    const savedItems = JSON.parse(localStorage.getItem('cartItems')) || [];
    setCartItems(savedItems);

    const timer = setTimeout(() => {
      navigate('/payment'); // 또는 결제 완료 페이지
    }, 3000);

    return () => clearTimeout(timer);
  }, [navigate]);

  return (
    <div className="min-h-screen flex flex-col justify-center items-center bg-white p-6">
      <h1 className="text-2xl font-bold mb-4">🛠 주문 처리 중입니다...</h1>
      <p className="text-gray-600 mb-6">아래 주문 내역을 확인해주세요</p>

      <ul className="mb-6">
        {cartItems.map((item) => (
          <li key={item.id}>
            {item.name} - {item.quantity}개
          </li>
        ))}
      </ul>

      <div className="text-4xl animate-bounce">⏳</div>
    </div>
  );
};

export default OrderProcessingPage;
