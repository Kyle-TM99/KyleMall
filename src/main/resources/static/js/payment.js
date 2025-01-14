$(document).ready(function () {
    $("#payBtnZipcode").click(payFindZipcode);

    document.getElementById('orderListPayment').addEventListener('click', function () {
        const IMP = window.IMP; // PortOne 라이브러리 초기화
        IMP.init('imp61718262'); // 가맹점 식별 코드 (PortOne 콘솔에서 확인)

        // 결제 데이터 설정
        const totalPrice = parseInt(document.getElementById('totalLastPrice').textContent.replace(/[^\d]/g, ''), 10);
        const deliveryName = document.getElementById('deliveryName').value;
        const payPhone = document.getElementById('payPhone1').value + '-' + document.getElementById('payPhone2').value + '-' + document.getElementById('payPhone3').value;
        const payAddress = document.getElementById('payAddress1').value + ' ' + document.getElementById('payAddress2').value;
        const paymentMethod = document.querySelector('input[name="radio_paymethod"]:checked').value;
		const orderMessage = document.getElementById("orderMessage").value;
        const deliveryMessage = document.getElementById("deliveryMessage").value;

        const productNames = Array.from(document.querySelectorAll('#cartList tr .fs-5.fw-bold.text-dark')).map(el => el.innerText);
        let productName = productNames[0];
        const productCount = productNames.length;

        if (productCount > 1) {
            productName += " 외 " + (productCount - 1) + "개"; // 상품 개수는 현재 상품을 제외해야 하므로 (productCount - 1)
        }

        if (!deliveryName || !payPhone || !payAddress || totalPrice <= 0) {
            alert('모든 정보를 입력하고 다시 시도하세요.');
            return;
        }

        // 고유 주문 번호 생성
        const merchantUid = 'order_' + new Date().getTime();

        IMP.request_pay({
            pg: paymentMethod,
            pay_method: paymentMethod,
            merchant_uid: merchantUid,
            name: productName,
            amount: totalPrice,
            buyer_name: deliveryName,
            buyer_tel: payPhone,
            buyer_addr: payAddress,
        }, function (rsp) {
            if (rsp.success) {
                // 결제 성공 시 서버로 데이터 전달
                fetch('/api/payment/verify', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        impUid: rsp.imp_uid,
                        merchantUid: rsp.merchant_uid,
                        totalPrice: totalPrice,
                        paymentMethod: paymentMethod,
                        recipientName: deliveryName,
                        address: payAddress,
                        phoneNumber: payPhone,
                        productTitle: productName,
						orderMsg: orderMessage,
						shippingMsg: deliveryMessage
                    })
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('결제 검증 실패');
                        }
                        return response.text(); // 서버에서 merchantUid를 텍스트로 반환
                    })
                    .then(merchantUid => {
                        alert('결제가 성공적으로 완료되었습니다.');
                        // 주문 완료 페이지로 이동하며 주문 번호 전달
                        location.href = `/orderComplete?merchantUid=${merchantUid}`;
                    })
                    .catch(error => {
                        alert('결제 처리 중 오류가 발생했습니다: ' + error.message);
                    });
            } else {
                alert('결제에 실패했습니다: ' + rsp.error_msg);
            }
        });
    });
});


function payFindZipcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			var addr = ''; // 주소 변수
			var extraAddr = ''; // 참고 항목 변수

			//사용자가 선택한 주소 타입과 상관없이 모두 도로명 주소로 처리            
			addr = data.roadAddress;

			if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
				extraAddr += data.bname;
			}
			// 건물명이 있고, 공동주택일 경우 추가한다.
			if (data.buildingName !== '' && data.apartment === 'Y') {
				extraAddr += (extraAddr !== '' ?
					', ' + data.buildingName : data.buildingName);
			}

			// 표시할 참고 항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
			if (extraAddr !== '') {
				extraAddr = ' (' + extraAddr + ')';
			}

			// 조합된 참고 항목을 상세주소에 추가한다.
			addr += extraAddr;


			// 우편번호와 주소 정보를 해당 입력상자에 출력한다.
			$("#payZipcode").val(data.zonecode);
			$("#payAddress1").val(addr);
			$("#old_payAddress").val(data.jibunAddress);

			// 커서를 상세주소 입력상자로 이동한다.
			$("#payAddress2").focus();

		}
	}).open();
}