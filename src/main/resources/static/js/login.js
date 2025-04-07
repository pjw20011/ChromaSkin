document.addEventListener('DOMContentLoaded', () => {
    // 메타 태그에서 메시지 값 가져오기
    const successMeta = document.querySelector('meta[name="successMessage"]');
    const alertMeta = document.querySelector('meta[name="alertMessage"]');

    const successMessage = successMeta ? successMeta.content.trim() : null;
    const alertMessage = alertMeta ? alertMeta.content.trim() : null;

    // 성공 메시지 및 알림 메시지 표시
    if (successMessage) {
        alert(successMessage);
    }

    if (alertMessage) {
        alert(alertMessage);
    }

    // URL 파라미터 처리
    const urlParams = new URLSearchParams(window.location.search);

    // 로그인 실패 시 알림 표시
    if (urlParams.has('error')) {
        alert("로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
        // URL 파라미터 제거
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});
