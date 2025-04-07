let countdownInterval;

$(document).ready(function() {
    const registeredEmail = $("#mail").data("registered-email");
    if (registeredEmail) {
        $("#mail").val(registeredEmail);
    }
});

function sendNumber() {
    $("#mail_number").css("display", "block");
    fetch('/api/v1/email/send', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            mail: $("#mail").val()
        })
    })
        .then(response => response.text())
        .then(data => {
            alert("인증번호 발송");
            $("#Confirm").attr("value", data);
            clearInterval(countdownInterval); // 기존 카운트다운 클리어
            startCountdown(300); // 새로운 5분 카운트다운 시작
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function confirmNumber() {
    let mail = $("#mail").val();
    let number = $("#number").val();

    fetch('/api/v1/email/verify', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            mail: mail,
            verifyCode: number
        })
    })
        .then(response => response.text())
        .then(data => {
            alert(data);
            if (data === "인증이 완료되었습니다.") {
                window.location.href = "/login";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function startCountdown(duration) {
    let timer = duration, minutes, seconds;
    const countdownElement = document.getElementById('countdown');

    countdownInterval = setInterval(() => {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        countdownElement.textContent = minutes + ":" + seconds;

        if (--timer < 0) {
            clearInterval(countdownInterval);
            countdownElement.textContent = "Expired";
        }
    }, 1000);
}
