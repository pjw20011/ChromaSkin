document.addEventListener('DOMContentLoaded', function () {
    // meta 태그에서 메시지를 가져옴
    const alertMeta = document.querySelector('meta[name="alertMessage"]');
    if (alertMeta) {
        const alertMessage = alertMeta.content.trim();
        if (alertMessage) {
            alert(alertMessage); // 알림창 출력
        }
    }
});
