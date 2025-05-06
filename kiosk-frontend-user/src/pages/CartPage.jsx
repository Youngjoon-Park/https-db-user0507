import React from 'react';
import { useNavigate } from 'react-router-dom';

function CartPage({ cartItems, updateQuantity, removeItem, clearCart }) {
  const navigate = useNavigate();

  const totalPrice = cartItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  const handleOrder = () => {
    if (cartItems.length === 0) {
      alert('장바구니가 비어 있습니다.');
      return;
    }
    localStorage.setItem('cartItems', JSON.stringify(cartItems));
    navigate('/processing');
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>🛒 장바구니</h2>

      {cartItems.length === 0 ? (
        <p>장바구니가 비어 있습니다.</p>
      ) : (
        <div>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            {cartItems.map((item) => (
              <li
                key={item.id}
                style={{
                  marginBottom: '15px',
                  borderBottom: '1px solid #ddd',
                  paddingBottom: '10px',
                }}
              >
                <strong>{item.name}</strong> <br />
                {item.price.toLocaleString()}원 × {item.quantity}개 ={' '}
                <strong>
                  {(item.price * item.quantity).toLocaleString()}원
                </strong>
                <div style={{ marginTop: '5px' }}>
                  <button onClick={() => updateQuantity(item.id, 1)}>➕</button>
                  <button
                    onClick={() => updateQuantity(item.id, -1)}
                    disabled={item.quantity <= 1}
                  >
                    ➖
                  </button>
                  <button onClick={() => removeItem(item.id)}>❌</button>
                </div>
              </li>
            ))}
          </ul>

          <h3 style={{ marginTop: '20px' }}>
            💰 총 합계:{' '}
            <span style={{ color: 'green', fontWeight: 'bold' }}>
              {totalPrice.toLocaleString()}원
            </span>
          </h3>

          <div style={{ marginTop: '10px' }}>
            <button onClick={clearCart}>🧹 장바구니 비우기</button>
            <button onClick={handleOrder} style={{ marginLeft: '10px' }}>
              ✅ 주문하기
            </button>
          </div>

          <div style={{ marginTop: '30px' }}>
            <h4 style={{ fontWeight: 'bold', fontSize: '18px' }}>
              🧾 주문 내역:
            </h4>
            <ul style={{ marginTop: '10px' }}>
              {cartItems.map((item) => (
                <li key={item.id}>
                  {item.name} - {item.quantity}개 /{' '}
                  {(item.price * item.quantity).toLocaleString()}원
                </li>
              ))}
            </ul>
          </div>
        </div>
      )}
    </div>
  );
}

export default CartPage;
