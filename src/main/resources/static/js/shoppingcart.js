$(document).ready(function() {
    // 초기 총 금액 계산
    updateTotalPrice();
    
    $('#payCart').on('click', function (event) {
        // cartList의 제품 존재 여부 확인
        const cartList = $('#cartList tr');
        
        if (cartList.length === 0) { // 장바구니가 비어있을 때
            alert("장바구니에 상품이 없습니다. 상품을 추가해주세요.");
            event.preventDefault(); // 기본 동작(페이지 이동) 막기
        } 
    });
    
    // 장바구니 비우기 클릭 시 이벤트 핸들러
    $(document).on("click", "#clearCart", function () {
        const userResponse = confirm("장바구니를 비우시겠습니까?");
        
        if (userResponse) {
            $.ajax({
                url: "clearCart.ajax",
                type: "POST",
                dataType: "json",
                success: function (resData) {
                    // 장바구니 목록 비우기
                    $("#cartList").empty();

                    let emptyMessage = `
                        <tr>
                            <td class="text-center pt-5 pb-4">
                                <p>장바구니에 추가한 물건이 존재하지 않습니다.</p>
                            </td>
                        </tr>`;
                    $("#cartList").append(emptyMessage);

                    // 가격 업데이트
                    updateTotalPrice();
                    
                    // 헤더 장바구니 카운트 업데이트
                    $("#shoppingCartQuantity").text(resData.cnt);
                },
                error: function (xhr, status, error) {
                    alert("장바구니 비우기에 실패했습니다.");
                    console.error(error);
                }
            });
        } else {
            return false;
        }
    });

    // 장바구니 제거 클릭 시 이벤트 핸들러
    $(document).on("click", "#deleteCart", function () {
        // 장바구니 상품 번호
        let cartNo = $(this).siblings("#cartNo").val();

        // AJAX 요청
        $.ajax({
            url: "deleteCart.ajax", // 요청을 처리할 서버의 URL
            method: "post",        // HTTP 메서드
            data: { no: cartNo },
            dataType: "json",
            success: function (resData) {
                console.log(resData);

                // 장바구니 목록 비우기
                $("#cartList").empty();

                if (resData.cartList && resData.cartList.length > 0) {
                    // 장바구니에 상품이 있을 경우
                    resData.cartList.forEach(function (v) {
                        let date = new Date(v.addedAt);
                        let strDate =
                            date.getFullYear() +
                            "-" +
                            String(date.getMonth() + 1).padStart(2, "0") +
                            "-" +
                            String(date.getDate()).padStart(2, "0") +
                            " " +
                            String(date.getHours()).padStart(2, "0") +
                            ":" +
                            String(date.getMinutes()).padStart(2, "0");

							let result = `
							    <tr>
							        <td>
							            <div class="d-flex align-items-center position-relative">
							                <div class="row top-0 end-0 m-2">
							                    <div class="col">
							                        <input type="hidden" id="cartNo" value="${v.cartId}" />
							                        <button id="deleteCart" type="button" class="btn btn-danger btn-sm">
							                            <i class="bi bi-x"></i>
							                        </button>
							                    </div>
							                </div>
							                <img class="card-img-top" 
							                    src="${v.imageUrl ? 'kylemallproducts/' + v.imageUrl : 'https://dummyimage.com/450x300/dee2e6/6c757d.jpg'}"
							                    style="width: 15%; height: 200px; margin-right: 20px;" />
							                <div style="flex-grow: 1;">
							                    <div class="row">
							                        <div class="col">
							                            <span class="badge bg-primary">상품 번호: ${v.productNo}</span>
							                        </div>
							                        <div class="col text-end">
							                            <span class="text-muted" style="font-size: 0.9rem;">장바구니 추가: ${strDate}</span>
							                        </div>
							                    </div>
							                    <div class="row">
							                        <div class="col my-2">
							                            <strong><a class="fs-5 text-decoration-none fw-bold text-dark" href="/productDetail?no=${v.productNo}">
							                                ${v.productName}
							                            </a></strong>
							                        </div>
							                    </div>
							                    <div class="row">
							                        <div class="col text-end text-muted">
							                            ${v.saleOk == 1
							                                ? `<div class="badge bg-dark text-white">Sale</div>
							                                   <div class="product-price" data-sale="true" data-price="${v.salePrice}">가격: ${v.salePrice}원</div>`
							                                : `<span class="product-price" data-sale="false" data-price="${v.productPrice}">가격: ${v.productPrice}원</span>`}
							                        </div>
							                    </div>
							                    <div class="row my-2">
							                        <div class="col text-end">
							                            <label for="quantity-${v.productNo}" class="form-label">수량</label>
							                            <input type="number" class="form-control text-end" id="quantity-${v.productNo}" name="quantity"
							                                value="${v.quantity}" min="1" style="max-width: 5rem; margin-left: auto; margin-right: 0;" readonly>
							                        </div>
							                    </div>
							                </div>
							            </div>
							        </td>
							    </tr>`;

                        $("#cartList").append(result);
                    });
                } else {
                    // 장바구니가 비었을 경우
                    let emptyMessage = `
                        <tr>
                            <td class="text-center pt-5 pb-4">
                                <p>장바구니에 추가한 물건이 존재하지 않습니다.</p>
                            </td>
                        </tr>`;
                    $("#cartList").append(emptyMessage);
                }

                // 총 가격 업데이트
                updateTotalPrice();
                
                // 헤더 장바구니 카운트 업데이트
                $("#shoppingCartQuantity").text(resData.cnt);
            },
            error: function (xhr, status, error) {
                alert("장바구니 삭제에 실패했습니다.");
                console.error(error);
            },
        });
    });

    // 장바구니 추가 시 이벤트 핸들러
    $("#addShoppingCart").click(function() {
        // 추가할 상품의 개수
        let quantity = $("#inputQuantity").val(); // 추가할 상품 수량 (입력 필드)

        // 사용자 ID
        let memberId = $("#memberId").val(); // 사용자 ID (hidden 필드)

        // 상품 번호
        let productNo = $("#productNo").val(); // 상품 번호 (hidden 필드)

        // 유효성 검사
        if (!quantity) {
            alert("개수 필수 정보를 확인해주세요.");
            return false;
        }
        if (!productNo) {
            alert("상품 번호 필수 정보를 확인해주세요.");
            return false;
        }
        if (!memberId) {
            alert("아이디 필수 정보를 확인해주세요.");
            return false;
        }

        // AJAX 요청
        $.ajax({
            url: "addCart.ajax", // 요청을 처리할 서버의 URL
            method: "post",   // HTTP 메서드
            data: {
                memberId: memberId,
                productNo: productNo,
                count: quantity
            },
            dataType: "json",
            success: function(response) {
                // 요청 성공 시
                alert("장바구니에 상품이 추가되었습니다!");
                // 장바구니 수량 업데이트
                $("#shoppingCartQuantity").text(response.count);
            },
            error: function(xhr, status, error) {
                // 요청 실패 시
                alert("장바구니 추가에 실패했습니다.");
                console.error(error);
            },
        });
    });

    // 총 결제 금액 업데이트 함수
    function updateTotalPrice() {
        let totalPrice = 0;

        // 각 상품의 수량과 가격 계산
        $(".table tr").each(function() {
            const quantityElement = $(this).find("input[id^='quantity-']"); // 수량 입력 필드
            const priceElement = $(this).find(".product-price"); // 가격 정보

            if (quantityElement.length && priceElement.length) {
                const quantity = parseInt(quantityElement.val()); // 수량
                const price = parseInt(priceElement.attr("data-price")); // 가격 가져오기

                if (!isNaN(price) && !isNaN(quantity)) {
                    totalPrice += price * quantity;
                }
            }
        });

        // 총 결제 금액 반영
        $("#totalPrice").text(`${totalPrice.toLocaleString()}원`);
    }
});
