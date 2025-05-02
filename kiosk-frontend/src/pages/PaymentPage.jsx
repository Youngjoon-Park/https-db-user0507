// 📁 src/pages/PaymentPage.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import api from '../api/axiosInstance'; // ✅ 실제 요청도 시도함

function PaymentPage() {
  const { orderId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const [paymentUrl, setPaymentUrl] = useState('');

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const totalAmountRaw = queryParams.get('totalAmount');
    const totalAmount = totalAmountRaw ? Number(totalAmountRaw) : 0;

    if (!orderId || isNaN(totalAmount) || totalAmount <= 0) {
      alert('❌ orderId나 totalAmount가 유효하지 않습니다.');
      return;
    }

    // ✅ 실제 API는 시도하고, 실패 시 페이크 URL로 대체
    const preparePayment = async () => {
      try {
        const response = await api.post(
          `/api/payment/ready`,
          {
            orderId: Number(orderId),
            totalAmount,
          },
          {
            headers: { 'Content-Type': 'application/json' },
            withCredentials: true,
          }
        );

        const data = response.data;
        console.log('✅ [서버 응답 전체]:', data);

        const redirectUrl =
          data.nextRedirectPcUrl ||
          data.nextRedirectMobileUrl ||
          data.next_redirect_pc_url ||
          data.next_redirect_mobile_url;

        if (redirectUrl) {
          setPaymentUrl(redirectUrl);
        } else {
          throw new Error('❌ 결제 URL 없음 (응답 값에 URL이 없습니다)');
        }
      } catch (err) {
        console.warn('❌ 결제 준비 실패. 페이크 URL로 대체함:', err);
        const fallbackUrl = `http://localhost:5173/payment/success?pg_token=FAKE&orderId=${orderId}`;
        setPaymentUrl(fallbackUrl);
      }
    };

    preparePayment();
  }, [orderId, location.search]);

  const handleMockPayment = () => {
    navigate(`/payment/success?pg_token=FAKE&orderId=${orderId}`);
  };

  return (
    <div className="p-4 text-center">
      <h2 className="text-2xl font-bold mb-4">💳 결제 준비 중입니다...</h2>

      {paymentUrl ? (
        <div>
          <p className="mb-4">
            📱 아래 QR코드를 휴대폰으로 스캔해서 결제를 진행하세요
          </p>
          <img
            src={`https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=${encodeURIComponent(
              paymentUrl
            )}`}
            alt="카카오페이 결제 QR코드"
            className="mx-auto"
          />

          <button
            onClick={handleMockPayment}
            className="mt-6 px-4 py-2 bg-green-600 text-white rounded"
          >
            🧪 테스트 결제 성공 처리
          </button>
        </div>
      ) : (
        <p>QR코드를 불러오는 중입니다...</p>
      )}
    </div>
  );
}

export default PaymentPage;
