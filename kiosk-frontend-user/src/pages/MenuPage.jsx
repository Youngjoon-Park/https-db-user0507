import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosInstance';

const MenuPage = ({ addToCart }) => {
  const navigate = useNavigate();
  const [menus, setMenus] = useState([]);
  const orderType = localStorage.getItem('orderType');

  useEffect(() => {
    api
      .get('/api/user/menus')
      .then((res) => setMenus(res.data))
      .catch((err) => console.error('❌ 메뉴 불러오기 실패', err));
  }, []);

  const goToCart = () => {
    navigate('/cart');
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6 text-center">
        ☕ 메뉴를 선택하세요
      </h1>
      <p className="text-center text-lg text-gray-700 mb-6">
        주문 유형:{' '}
        <span className="font-bold">
          {orderType === 'store' ? '매장 식사' : '포장 주문'}
        </span>
      </p>

      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {menus.map((item) => (
          <div
            key={item.id}
            className="bg-white rounded-2xl shadow-lg p-4 cursor-pointer hover:shadow-xl transition-all"
            onClick={() => addToCart(item)}
          >
            <img
              src={`https://kiosktest.shop/uploads/${item.image}`}
              alt={item.name}
              className="w-full h-32 object-cover rounded-xl mb-3"
            />
            <h2 className="text-xl font-semibold mb-1 text-center">
              {item.name}
            </h2>
            <p className="text-center text-gray-700">
              {item.price.toLocaleString()}원
            </p>
          </div>
        ))}
      </div>

      <div className="mt-8 text-center">
        <button
          onClick={goToCart}
          className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-4 text-lg font-semibold rounded-2xl transition-all"
        >
          장바구니 보기
        </button>
      </div>
    </div>
  );
};

export default MenuPage;
