// loading.js
document.addEventListener("DOMContentLoaded", () => {
    const inputFile = document.getElementById("input-file");
    const fileInfo = document.getElementById("file-info");
    const fileName = document.getElementById("file-name");
    const checkBtn = document.getElementById("check-btn");
    const loadingOverlay = document.getElementById("loading-overlay");

    // 파일이 선택되면 file-info를 표시하고 파일 이름을 업데이트
    inputFile.addEventListener("change", (event) => {
        const selectedFile = event.target.files[0];
        if (selectedFile) {
            fileName.textContent = selectedFile.name;
            fileInfo.style.display = "block";
        }
    });

    // 검사하기 버튼을 클릭하면 로딩 오버레이 표시
    checkBtn.addEventListener("click", (event) => {
        loadingOverlay.style.display = "flex";
    });
});
