// 스크롤을 올리는 함수
function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// 스크롤 위치에 따라 버튼 보이기
window.onscroll = function() {
    const topButton = document.getElementById("topButton");
    if (document.body.scrollTop > 100 || document.documentElement.scrollTop > 100) {
        topButton.style.display = "block";
    } else {
        topButton.style.display = "none";
    }
};

// 좋아요 버튼 클릭 이벤트 처리
function handleLikeButtonClick(event, element) {
    // 부모 a 태그로 이벤트 전파를 중단
    event.stopPropagation();
    event.preventDefault();

    // HTML에서 cosmeticsId 가져오기
    const cosmeticsId = element.closest(".product-item").getAttribute("data-id");

    // cosmeticsId 유효성 확인
    if (!cosmeticsId) {
        console.error("cosmeticsId가 유효하지 않습니다. data-id를 확인하세요.");
        return;
    }

    // 서버에 요청 보내기
    fetch('/cosmetics/toggle-cosmetics-great', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({ cosmeticsId: cosmeticsId }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`좋아요 요청이 실패했습니다. 상태 코드: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("좋아요 응답 데이터:", data); // 응답 데이터 출력

            // 좋아요 상태에 따라 이미지 업데이트
            const img = element.querySelector('img');
            const timestamp = new Date().getTime(); // 캐시 방지용 타임스탬프 추가
            img.src = data.liked
                ? `/image/cosmetics_great_af_icon.png?${timestamp}`
                : `/image/cosmetics_great_be_icon.png?${timestamp}`;
        })
        .catch(error => {
            console.error('좋아요 요청 중 오류:', error);
        });
}


// 페이지 로드 후 좋아요 버튼 이벤트 설정
document.addEventListener('DOMContentLoaded', () => {
    document.body.addEventListener('click', (event) => {
        const button = event.target.closest('.great-button');
        if (button) {
            handleLikeButtonClick(event, button);
        }
    });
});



