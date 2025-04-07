// 파일 선택 시 파일 정보 표시 및 제출 버튼 활성화
function showFileInfo() {
    const fileInput = document.getElementById('input-file');
    const fileName = document.getElementById('file-name');
    const fileInfoDiv = document.getElementById('file-info');

    if (fileInput.files.length > 0) {
        fileName.textContent = `선택한 파일: ${fileInput.files[0].name}`;
        fileInfoDiv.style.display = 'block';
    }
}

// 모달 열기
function openWebcamModal() {
    const webcamModal = document.getElementById('webcamModal');
    webcamModal.style.display = 'flex';

    // 기존 스트림 종료
    if (window.currentStream) {
        window.currentStream.getTracks().forEach(track => track.stop());
    }

    // 새 스트림 시작
    navigator.mediaDevices.getUserMedia({ video: true })
        .then(stream => {
            window.currentStream = stream; // 현재 스트림 저장
            const webcam = document.getElementById('webcam');
            webcam.srcObject = stream;
        })
        .catch(err => {
            console.error('웹캠 접근 오류:', err);
            alert('웹캠에 접근할 수 없습니다. 에러: ' + err.name);
        });
}


// 모달 닫기
function closeWebcamModal() {
    const webcamModal = document.getElementById('webcamModal');
    webcamModal.style.display = 'none';

    const webcam = document.getElementById('webcam');
    if (webcam.srcObject) {
        const stream = webcam.srcObject;
        stream.getTracks().forEach(track => track.stop()); // 모든 트랙 종료
        webcam.srcObject = null; // 스트림 해제
    }
}


// 웹캠 촬영하기
document.getElementById('capture-webcam').addEventListener('click', () => {
    const webcam = document.getElementById('webcam');
    const canvas = document.getElementById('canvas');
    const context = canvas.getContext('2d');

    // 캔버스 크기 설정 (웹캠 크기에 맞추기)
    canvas.width = webcam.videoWidth;
    canvas.height = webcam.videoHeight;

    // 현재 웹캠 화면을 캔버스에 그리기
    context.drawImage(webcam, 0, 0, canvas.width, canvas.height);

    // 캔버스를 Blob으로 변환하여 서버에 전송
    canvas.toBlob(blob => {
        const fileInput = document.getElementById('input-file');
        const dataTransfer = new DataTransfer();
        const file = new File([blob], 'webcam_image.jpg', { type: 'image/jpeg' });

        dataTransfer.items.add(file);
        fileInput.files = dataTransfer.files;

        // 파일 정보 표시 및 모달 닫기
        showFileInfo();
        closeWebcamModal();
    }, 'image/jpeg');
});