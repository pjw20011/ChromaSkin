const videoElement = document.createElement('video');
videoElement.setAttribute('autoplay', '');
videoElement.setAttribute('playsinline', '');
videoElement.width = 700;
videoElement.height = 500;
document.getElementById('comparisonWrapper').appendChild(videoElement);

const comparisonCanvas = document.getElementById('comparisonCanvas');
const comparisonCtx = comparisonCanvas.getContext('2d');

let currentMakeupType = null;
let currentFoundationColor = null;
let currentLipstickColor = null;

// Mediapipe FaceMesh Setup
const faceMesh = new FaceMesh({
    locateFile: (file) => `https://cdn.jsdelivr.net/npm/@mediapipe/face_mesh/${file}`
});

faceMesh.setOptions({
    maxNumFaces: 1,
    refineLandmarks: true,
    minDetectionConfidence: 0.7,
    minTrackingConfidence: 0.7
});

faceMesh.onResults(onResults);

const camera = new Camera(videoElement, {
    onFrame: async () => {
        await faceMesh.send({ image: videoElement });
    },
    width: 700,
    height: 500
});
camera.start();

// Makeup Type Selection
function selectMakeupType(type) {
    currentMakeupType = type;
    updatePalette();
}
// function selectMakeupType(type) {
//     currentMakeupType = type;
//
//     // 버튼 요소 가져오기
//     const foundationButton = document.getElementById('saveFoundationButton');
//     const lipstickButton = document.getElementById('saveLipstickButton');
//
//     // 초기 상태에서 모든 버튼 숨기기
//     foundationButton.hidden = true;
//     lipstickButton.hidden = true;
//
//     // 선택된 타입에 따라 버튼 표시
//     if (type === 'foundation') {
//         foundationButton.hidden = false; // 파운데이션 버튼 표시
//     } else if (type === 'lipstick') {
//         lipstickButton.hidden = false; // 립스틱 버튼 표시
//     }
//
//     // 팔레트 업데이트
//     updatePalette();
// }



function updatePalette() {
    const paletteElement = document.getElementById('palette');
    paletteElement.innerHTML = '';
    const nameButtonsContainer = document.getElementById('nameButtons');
    nameButtonsContainer.innerHTML = '';

    if (currentMakeupType === 'foundation') {
        const colors = ['#fbe8d3', '#f5d7bb', '#e7c5a6', '#dab08b', '#c69675', '#b58364', '#9f6c50'];
        const labels = ['13호', '17호', '21호', '23호', '25호', '27호', '30호'];

        colors.forEach((color, index) => {
            const colorDiv = document.createElement('div');
            colorDiv.className = 'color-option';
            colorDiv.style.backgroundColor = color;
            colorDiv.textContent = labels[index];
            colorDiv.onclick = () => {
                currentFoundationColor = color;
                console.log('Selected Foundation Color:', color);
            };
            paletteElement.appendChild(colorDiv);
        });
    } else if (currentMakeupType === 'lipstick') {
        Object.keys(groupedLipsticks).forEach((groupName) => {
            const button = document.createElement('button');
            button.className = 'brand-button';
            button.textContent = groupName;
            button.onclick = () => showColorsForGroup(groupName);
            nameButtonsContainer.appendChild(button);
        });
    }
}

function showColorsForGroup(groupName) {
    const paletteElement = document.getElementById('palette');
    paletteElement.innerHTML = '';
    groupedLipsticks[groupName].forEach((lipstick) => {
        const imageDiv = document.createElement('div');
        imageDiv.className = 'color-option';
        imageDiv.style.backgroundImage = `url(${lipstick.colorimage})`;
        imageDiv.style.backgroundSize = 'cover';
        imageDiv.style.backgroundPosition = 'center';
        imageDiv.title = `${lipstick.name} - ${lipstick.colorname}`;
        imageDiv.onclick = () => {
            currentLipstickColor = lipstick.colorcode;
            console.log('Selected Lipstick Color:', lipstick.colorcode);
        };
        paletteElement.appendChild(imageDiv);
    });
}

function onResults(results) {
    comparisonCtx.clearRect(0, 0, comparisonCanvas.width, comparisonCanvas.height);
    comparisonCtx.drawImage(results.image, 0, 0, comparisonCanvas.width, comparisonCanvas.height);

    if (results.multiFaceLandmarks) {
        for (const landmarks of results.multiFaceLandmarks) {
            if (currentFoundationColor) applyFoundation(landmarks, comparisonCanvas.width, comparisonCanvas.height);
            if (currentLipstickColor) applyLipstick(landmarks);
        }
    }
}


function applyFoundation(landmarks) {
    const foundationColorRGB = hexToRGB(currentFoundationColor);
    comparisonCtx.fillStyle = `rgba(${foundationColorRGB.r}, ${foundationColorRGB.g}, ${foundationColorRGB.b}, 0.35)`;

    // Mediapipe 랜드마크 정의
    const faceOutlineIndices = [
        10, 338, 297, 332, 284, 251, 389, 264, 447, 454, 323,
        361, 288, 397, 365, 379, 378, 400, 377, 152, 148, 176,
        149, 150, 136, 172, 58, 132, 93, 234, 127, 162, 21, 54,
        103, 67, 109
    ];

    const excludedRegions = [
        // 왼쪽 눈 (범위 확대)
        [33, 246, 161, 160, 159, 158, 157, 173, 144, 145, 153, 154, 155, 133, 7, 163],
        // 오른쪽 눈 (범위 확대)
        [362, 263, 387, 386, 385, 384, 398, 373, 374, 380, 381, 382, 362, 249],
        // 왼쪽 눈썹 (범위 확대)
        [70, 46, 53, 52, 65, 55, 107, 66, 105, 63],
        // 오른쪽 눈썹 (범위 확대)
        [336, 296, 334, 293, 300, 276, 283, 282, 295, 285],
        // 입술 외부
        [61, 185, 40, 39, 37, 0, 267, 269, 270, 409, 291, 375, 321, 405, 314, 17, 84, 181, 91, 146, 61],
        // 입술 내부
        [78, 95, 88, 178, 87, 14, 317, 402, 318, 324, 308]
    ];

    // 얼굴 외곽선
    const faceOutline = faceOutlineIndices.map(index => ({
        x: landmarks[index].x * comparisonCanvas.width,
        y: landmarks[index].y * comparisonCanvas.height
    }));

    // 얼굴 외곽선 그리기: 첫 번째 블러 처리
    comparisonCtx.beginPath();
    faceOutline.forEach((point, index) => {
        if (index === 0) {
            comparisonCtx.moveTo(point.x, point.y);
        } else {
            comparisonCtx.lineTo(point.x, point.y);
        }
    });
    comparisonCtx.closePath();
    comparisonCtx.filter = 'blur(70px)'; // 첫 번째 블러
    comparisonCtx.fill();
    comparisonCtx.filter = 'none';

    // 얼굴 외곽선 그리기: 두 번째 블러 처리
    comparisonCtx.globalAlpha = 0.9; // 투명도 조정
    comparisonCtx.filter = 'blur(100px)'; // 두 번째 블러
    comparisonCtx.fill();
    comparisonCtx.filter = 'none';
    comparisonCtx.globalAlpha = 1.0; // 투명도 복구

    // 제외 영역 처리
    comparisonCtx.globalCompositeOperation = 'destination-out'; // 제외 영역 투명 처리
    excludedRegions.forEach(region => {
        comparisonCtx.beginPath();
        region.forEach((index, pointIndex) => {
            const point = {
                x: landmarks[index].x * comparisonCanvas.width,
                y: landmarks[index].y * comparisonCanvas.height
            };
            if (pointIndex === 0) {
                comparisonCtx.moveTo(point.x, point.y);
            } else {
                comparisonCtx.lineTo(point.x, point.y);
            }
        });
        comparisonCtx.closePath();
        comparisonCtx.filter = 'blur(15px)'; // 제외 영역 블러 처리
        comparisonCtx.fill();
        comparisonCtx.filter = 'none';
    });
    comparisonCtx.globalCompositeOperation = 'source-over'; // 기본 상태 복구
}


function applyLipstick(landmarks) {
    const lipColorRGB = hexToRGB(currentLipstickColor);
    comparisonCtx.fillStyle = applyHSVAdjustment(lipColorRGB.r, lipColorRGB.g, lipColorRGB.b);

    const upperLip = [ 0, 267, 269, 270, 409, 291, 375, 321, 405, 314, 17, 84, 181, 91, 146, 61, 185, 40, 39, 37];
    const lowerLip = [78, 95, 88, 178, 87, 14, 317, 402, 318, 324, 308, 375, 321, 405, 314, 17, 84, 181, 91, 146];

    drawLipArea(upperLip, landmarks);
    drawLipArea(lowerLip, landmarks);
    applyInnerLipTransparency(landmarks);

}

function drawLipArea(lipIndices, landmarks) {
    comparisonCtx.beginPath();
    for (let i = 0; i < lipIndices.length; i++) {
        const point = landmarks[lipIndices[i]];
        const x = point.x * comparisonCanvas.width;
        const y = point.y * comparisonCanvas.height;
        if (i === 0) comparisonCtx.moveTo(x, y);
        else comparisonCtx.lineTo(x, y);
    }
    comparisonCtx.closePath();
    comparisonCtx.filter = 'blur(1.5px)';
    comparisonCtx.fill();
    comparisonCtx.filter = 'none';
}

function applyInnerLipTransparency(landmarks) {
    const innerLip = [78, 191, 80, 81, 82, 13, // 윗입술 안쪽
        312, 311, 310, 415, 308, // 아랫입술 안쪽
        14, 87, 178, 88, 95,]; // 업데이트된 중앙 점들 포함
    comparisonCtx.globalCompositeOperation = 'destination-out';
    comparisonCtx.beginPath();
    for (let i = 0; i < innerLip.length; i++) {
        const point = landmarks[innerLip[i]];
        const x = point.x * comparisonCanvas.width;
        const y = point.y * comparisonCanvas.height;
        if (i === 0) comparisonCtx.moveTo(x, y);
        else comparisonCtx.lineTo(x, y);
    }
    comparisonCtx.closePath();
    comparisonCtx.filter = 'blur(1.5px)';
    comparisonCtx.fill();
    comparisonCtx.globalCompositeOperation = 'source-over';
    comparisonCtx.filter = 'none';
}

function hexToRGB(hex) {
    const r = parseInt(hex.slice(1, 3), 16);
    const g = parseInt(hex.slice(3, 5), 16);
    const b = parseInt(hex.slice(5, 7), 16);
    return { r, g, b };
}

function applyHSVAdjustment(r, g, b) {
    let hsv = rgbToHsv(r, g, b);
    hsv[1] = Math.min(hsv[1] * 1.2, 1);
    hsv[2] = Math.max(hsv[2] * 0.8, 0);
    const rgb = hsvToRgb(hsv[0], hsv[1], hsv[2]);
    return `rgba(${rgb[0]}, ${rgb[1]}, ${rgb[2]}, 0.23)`;
}

function rgbToHsv(r, g, b) {
    r /= 255;
    g /= 255;
    b /= 255;
    const max = Math.max(r, g, b),
        min = Math.min(r, g, b);
    let h, s, v = max;
    const d = max - min;
    s = max === 0 ? 0 : d / max;
    if (max === min) h = 0;
    else {
        switch (max) {
            case r:
                h = (g - b) / d + (g < b ? 6 : 0);
                break;
            case g:
                h = (b - r) / d + 2;
                break;
            case b:
                h = (r - g) / d + 4;
                break;
        }
        h /= 6;
    }
    return [h, s, v];
}

function hsvToRgb(h, s, v) {
    let r, g, b;
    const i = Math.floor(h * 6);
    const f = h * 6 - i;
    const p = v * (1 - s);
    const q = v * (1 - f * s);
    const t = v * (1 - (1 - f) * s);
    switch (i % 6) {
        case 0:
            (r = v), (g = t), (b = p);
            break;
        case 1:
            (r = q), (g = v), (b = p);
            break;
        case 2:
            (r = p), (g = v), (b = t);
            break;
        case 3:
            (r = p), (g = q), (b = v);
            break;
        case 4:
            (r = t), (g = p), (b = v);
            break;
        case 5:
            (r = v), (g = p), (b = q);
            break;
    }
    return [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)];
}



// 파운데이션 저장
function saveSelectedFoundation(foundationLabel, color) {
    if (!foundationLabel || !color) {
        alert('파운데이션 색상을 선택하세요.');
        return;
    }

    fetch('/save-foundation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            foundationLabel: foundationLabel,
            color: color,
            nickname: userNickname // 세션에서 전달된 nickname 사용
        })
    })
        .then(response => response.json())
        .then(data => {
            console.log('Foundation saved:', data);
            alert('파운데이션 정보가 저장되었습니다!');
        })
        .catch(error => {
            console.error('Error saving foundation:', error);
            alert('파운데이션 저장에 실패했습니다. 다시 시도해주세요.');
        });
}

// 립스틱 저장
function saveSelectedLipstick(lipstickId, colorImage, lipName, lipBrand, lipColorCode) {
    if (!lipstickId || !colorImage || !lipName || !lipBrand || !lipColorCode) {
        alert('립스틱 색상을 선택하세요.');
        return;
    }

    fetch('/save-lipstick', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            lipstickId: lipstickId,
            colorImage: colorImage,
            lipName: lipName,
            lipBrand: lipBrand,
            lipColorCode: lipColorCode,
            nickname: userNickname // 세션에서 전달된 nickname 사용
        })
    })
        .then(response => response.json())
        .then(data => {
            console.log('Lipstick saved:', data);
            alert('립스틱 정보가 저장되었습니다!');
        })
        .catch(error => {
            console.error('Error saving lipstick:', error);
            alert('립스틱 저장에 실패했습니다. 다시 시도해주세요.');
        });
}
