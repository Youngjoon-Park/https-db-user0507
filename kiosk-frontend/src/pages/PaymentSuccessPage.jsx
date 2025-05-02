import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import api from '../api/axiosInstance'; // ✅ 실제 요청 테스트용

function PaymentSuccessPage() {
  const [searchParams] = useSearchParams();
  const [message, setMessage] = useState('결제 승인 중...');

  useEffect(() => {
    const pgToken = searchParams.get('pg_token');
    const orderId = searchParams.get('orderId');

    if (!pgToken || !orderId) {
      setMessage('❌ 결제 정보가 없습니다. 다시 시도해주세요.');
      return;
    }

    const approvePayment = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await api.post(
          '/api/payment/approve',
          {
            pgToken,
            orderId: Number(orderId),
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const redirectUrl = response.data?.nextRedirectPcUrl;
        if (redirectUrl) {
          if (!redirectUrl.includes('localhost')) {
            console.log('✅ 리디렉션 URL로 이동:', redirectUrl);
            window.location.href = redirectUrl; // ✅ 운영 배포면 이동
          } else {
            console.log('✅ [로컬 테스트용] redirect 생략');
            setMessage('✅ [테스트용] 결제가 완료되었습니다!');
          }
        } else {
          setMessage('✅ [테스트용] 결제가 완료되었습니다!');
        }
      } catch (err) {
        console.error('❌ 결제 승인 실패:', err);
        setMessage('❌ 결제 승인에 실패했습니다. 관리자에게 문의하세요.');
      }
    };

    approvePayment();
  }, [searchParams]);

  return (
    <div className="p-4 text-center">
      <h2 className="text-2xl font-bold mb-4">{message}</h2>
      <p>이용해 주셔서 감사합니다.</p>
    </div>
  );
}

export default PaymentSuccessPage;
