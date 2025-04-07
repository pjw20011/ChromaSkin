document.addEventListener('DOMContentLoaded', function () {
    // Meta 태그에서 서버 메시지 읽기
    const alertMeta = document.querySelector('meta[name="alertMessage"]');
    if (alertMeta) {
        const alertMessage = alertMeta.content.trim();
        if (alertMessage) {
            alert(alertMessage); // 메시지를 알림창으로 출력
        }
    }

    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const confirmPasswordError = document.getElementById('confirm-password-error');

    password.addEventListener('input', () => toggleEyeIcon(password, 'passwordIcon'));
    confirmPassword.addEventListener('input', () => toggleEyeIcon(confirmPassword, 'confirmPasswordIcon'));

    confirmPassword.addEventListener('blur', () => {
        if (password.value !== confirmPassword.value) {
            confirmPasswordError.textContent = "비밀번호가 일치하지 않습니다.";
        } else {
            confirmPasswordError.textContent = "";
        }
    });

    document.getElementById('username').addEventListener('blur', checkUsername);
    document.getElementById('nickname').addEventListener('blur', checkNickname);
    document.getElementById('email').addEventListener('blur', checkEmail);

    flatpickr("#birthDate", {
        dateFormat: "Y-m-d",
        altInput: true,
        altFormat: "Y년 m월 d일",
    });
});

function togglePassword(fieldId, icon) {
    const field = document.getElementById(fieldId);
    if (field.type === "password") {
        field.type = "text";
        icon.classList.remove("fa-eye");
        icon.classList.add("fa-eye-slash");
    } else {
        field.type = "password";
        icon.classList.remove("fa-eye-slash");
        icon.classList.add("fa-eye");
    }
}

function toggleEyeIcon(field, iconId) {
    const icon = document.getElementById(iconId);
    if (field.value) {
        icon.style.display = 'inline';
    } else {
        icon.style.display = 'none';
    }
}

function checkUsername() {
    const username = document.getElementById('username').value;
    fetch(`/api/users/check-username?username=${username}`)
        .then(response => response.json())
        .then(data => {
            const usernameError = document.getElementById('username-error');
            if (data === false) {
                usernameError.textContent = "아이디가 이미 존재합니다.";
            } else {
                usernameError.textContent = "";
            }
        });
}

function checkNickname() {
    const nickname = document.getElementById('nickname').value;
    fetch(`/api/users/check-nickname?nickname=${nickname}`)
        .then(response => response.json())
        .then(data => {
            const nicknameError = document.getElementById('nickname-error');
            if (data === false) {
                nicknameError.textContent = "닉네임이 이미 존재합니다.";
            } else {
                nicknameError.textContent = "";
            }
        });
}

function checkEmail() {
    const email = document.getElementById('email').value;
    fetch(`/api/users/check-email?email=${email}`)
        .then(response => response.json())
        .then(data => {
            const emailError = document.getElementById('email-error');
            if (data === false) {
                emailError.textContent = "이메일이 이미 존재합니다.";
            } else {
                emailError.textContent = "";
            }
        });
}
