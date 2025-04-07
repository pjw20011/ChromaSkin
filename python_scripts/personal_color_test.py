import cv2
import dlib
import numpy as np
import sys
from collections import OrderedDict
import os

# 현재 스크립트의 디렉토리 경로를 기준으로 .dat 파일 경로 설정
script_dir = os.path.dirname(os.path.abspath(__file__))
predictor_path = os.path.join(script_dir, 'shape_predictor_68_face_landmarks.dat')

# Dlib 얼굴 검출기와 랜드마크 예측기 로드
face_detector = dlib.get_frontal_face_detector()
landmark_predictor = dlib.shape_predictor(predictor_path)

# 퍼스널 컬러 색상 컬렉션 (BGR 순서)
lip_colors = OrderedDict({
    1: (166, 182, 248),  # 봄: 피치 핑크 (BGR: 166, 182, 248)
    2: (187 ,164, 249),  # 여름: 로즈 핑크 (187 ,164, 249)
    3: (137 ,162, 254),   # 가을: 살몬 핑크 (BGR: 137 ,162, 254)
    4: (195, 101, 255)     # 겨울: 마젠타 (BGR: 195, 101, 255)
})

# 피부, 머리카락, 눈동자 색상 비교용 컬러 옵션 (BGR 순서)
skin_colors = [(241, 211, 175), (243, 206, 187)]
hair_colors = [(137, 85, 48), (34, 34, 34), (63, 44, 30), (6, 4, 5)]
eye_colors = [(137, 85, 48), (34, 34, 34), (63, 44, 30), (6, 4, 5)]

# 얼굴 영역을 위한 랜드마크 인덱스
lip_indices = list(range(48, 68))
skin_indices = list(range(0, 17))  # 턱선 영역을 피부로 가정
hair_indices = list(range(17, 27))  # 이마 위의 머리카락 영역
eye_indices = list(range(36, 48))   # 눈 영역

# 파이썬 스크립트 인자로 받은 값 처리
image_path = sys.argv[1]
color_key = int(sys.argv[2])

# 이미지 읽기
image = cv2.imread(image_path)

# 얼굴 검출
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
faces = face_detector(gray)

def get_average_color(points, landmarks, image):
    """지정된 랜드마크 포인트들로부터 평균 색상을 추출"""
    h, w, _ = image.shape
    colors = []
    for i in points:
        x = landmarks.part(i).x
        y = landmarks.part(i).y
        colors.append(image[y, x])
    return np.mean(colors, axis=0)

def compare_colors(color1, color2):
    """두 색상의 유사도를 계산 (Euclidean distance)"""
    return np.linalg.norm(np.array(color1) - np.array(color2))

for face in faces:
    # 랜드마크 추정
    landmarks = landmark_predictor(gray, face)

    # 입술 부분의 랜드마크 좌표 추출
    lip_points = [(landmarks.part(i).x, landmarks.part(i).y) for i in lip_indices]

    # 입술에 색상 적용
    mask = np.zeros_like(image, dtype=np.uint8)
    cv2.fillPoly(mask, [np.array(lip_points, dtype=np.int32)], (255, 255, 255))

    # 마스크의 가장자리 부드럽게 처리
    blurred_mask = cv2.GaussianBlur(mask, (3, 3), 0)  # 커널 크기를 조절할 수 있습니다

    lip_color = lip_colors[color_key]

    # BGR에서 HSV로 변환
    hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    hsv_color = cv2.cvtColor(np.uint8([[lip_color]]), cv2.COLOR_BGR2HSV)[0][0]

    # 입술 색상 적용
    lip_hue = hsv_color[0]
    lip_saturation = hsv_color[1]

    # 기존 밝기(V 채널)를 유지하고, Hue와 Saturation만 변경
    original_value = hsv_image[:, :, 2].copy()  # 원래 밝기 값 복사

    hsv_image[:, :, 0] = np.where(blurred_mask[:, :, 0] == 255, lip_hue, hsv_image[:, :, 0])  # 색조 적용
    lip_saturation = min(hsv_color[1] + 100, 255)  # 기존 채도 값에 50을 더하되 최대 255로 제한
    hsv_image[:, :, 1] = np.where(blurred_mask[:, :, 0] == 255, lip_saturation, hsv_image[:, :, 1])  # 채도 적용
    hsv_image[:, :, 2] = original_value  # 밝기 원상 복구

    # 다시 BGR로 변환
    colored_frame = cv2.cvtColor(hsv_image, cv2.COLOR_HSV2BGR)

    # 원본 이미지와 색상을 적용한 이미지를 부드럽게 혼합
    combined_frame = cv2.addWeighted(image, 0.2, colored_frame, 0.8, 0)  # 원본 비율을 0.8로 설정

    # 이미지 저장 경로 설정
    output_path = f"Demo_Ver/Demo_ver/src/main/resources/static/output_image/image_{color_key}.jpg"
    cv2.imwrite(output_path, combined_frame)

    # 피부, 머리카락, 눈동자 색상 추출
    skin_color = get_average_color(skin_indices, landmarks, image)
    hair_color = get_average_color(hair_indices, landmarks, image)
    eye_color = get_average_color(eye_indices, landmarks, image)

    # 각각의 색상과 비교하여 가장 가까운 색상 선택
    closest_skin_color = min(skin_colors, key=lambda x: compare_colors(skin_color, x))
    closest_hair_color = min(hair_colors, key=lambda x: compare_colors(hair_color, x))
    closest_eye_color = min(eye_colors, key=lambda x: compare_colors(eye_color, x))

    # 인덱스 추출
    skin_index = skin_colors.index(closest_skin_color) + 1
    hair_index = hair_colors.index(closest_hair_color) + 1
    eye_index = eye_colors.index(closest_eye_color) + 1

    # 결과 출력 (Java에서 처리할 수 있도록)
    print(f"Image saved at {output_path}")
    print(f"Skin: {skin_index}")
    print(f"Hair: {hair_index}")
    print(f"Eye: {eye_index}")
