const slideFrame = document.querySelector('.slide-track');
const prevBtn = document.querySelector('.prev');
const nextBtn = document.querySelector('.next');
const slides = document.querySelectorAll('.session1-img');
const totalSlides = slides.length;
let currentSlide = 0;

// 슬라이드를 이동시키는 함수
const moveSlide = (index) => {
    slideFrame.style.transform = `translateX(${-index * 100}%)`; // 슬라이드를 이동시키기
    currentSlide = index;
    console.log(`Slide moved to index: ${index}`); // 디버깅을 위한 로그
};

// 자동 슬라이드 설정 (5초마다 이동)
setInterval(() => {
    console.log("Auto-slide is running"); // 자동 슬라이드 작동 확인을 위한 로그
    if (currentSlide === totalSlides - 1) {
        moveSlide(0); // 마지막 슬라이드에서 첫 번째로 이동
    } else {
        moveSlide(currentSlide + 1); // 다음 슬라이드로 이동
    }
}, 5000);

// 로그인
// 로그인 성공 메시지 처리
window.onload = function () {
    // URL 파라미터 기반 메시지 처리
    const urlParams = new URLSearchParams(window.location.search);

    // 로그인 성공 시 alert 띄우기
    if (urlParams.has('success')) {
        alert("로그인 성공!");
        // URL에서 success 파라미터 제거
        window.history.replaceState({}, document.title, window.location.pathname);
    }

    // successMessage 처리
    const successMessageElement = document.querySelector('#successMessage');
    if (successMessageElement) {
        alert(successMessageElement.textContent); // 알림창 출력
        successMessageElement.remove(); // DOM에서 메시지 제거
    }
};
