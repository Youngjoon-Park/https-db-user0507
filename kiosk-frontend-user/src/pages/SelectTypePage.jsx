// 📁 src/pages/SelectTypePage.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const SelectTypePage = () => {
  const navigate = useNavigate();

  const handleSelect = (type) => {
    localStorage.setItem('orderType', type);
    navigate('/menu');
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-6">
      <h1 className="text-3xl font-bold mb-10 text-center">
        주문 유형을 선택해주세요
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 w-full max-w-3xl">
        <div
          className="bg-white border-4 border-gray-300 shadow-lg rounded-2xl p-10 text-center cursor-pointer hover:bg-gray-50 hover:scale-105 transition transform"
          onClick={() => handleSelect('store')}
        >
          <div className="text-6xl mb-4">🏠</div>
          <h2 className="text-2xl font-bold mb-2">매장 식사</h2>
          <p className="text-gray-600">매장에서 드실게요</p>
        </div>

        <div
          className="bg-white border-4 border-gray-300 shadow-lg rounded-2xl p-10 text-center cursor-pointer hover:bg-gray-50 hover:scale-105 transition transform"
          onClick={() => handleSelect('takeout')}
        >
          <div className="text-6xl mb-4">📦</div>
          <h2 className="text-2xl font-bold mb-2">포장 주문</h2>
          <p className="text-gray-600">포장해서 갈게요</p>
        </div>
      </div>
    </div>
  );
};

export default SelectTypePage;
