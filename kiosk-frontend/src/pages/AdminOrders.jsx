import { useEffect, useState } from 'react';
import api from '../api/axiosInstance';

function AdminOrders() {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    api
      .get('/api/admin/orders')
      .then((res) => {
        setOrders(res.data);
        setError('');
      })
      .catch((err) => {
        console.error('❌ 주문 불러오기 실패:', err);
        setError('인증 오류 또는 서버 오류가 발생했습니다.');
      });
  }, []);

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">🧾 관리자 주문 목록</h2>

      {error && <div className="text-red-500">{error}</div>}

      <ul>
        {orders.map((order) => (
          <li key={order.id} className="border p-2 mb-2">
            <p>🧍 주문자: {order.customerName}</p>
            <p>📦 상태: {order.status}</p>
            <p>🕒 시간: {order.createdAt}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AdminOrders;
