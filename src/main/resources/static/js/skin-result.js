document.addEventListener('DOMContentLoaded', () => {
    const ingredientDescriptions = {
        // 건성 좋은 성분
        '히아루론산': '히아루론산은 피부에 수분을 공급하고 보습 효과가 뛰어난 성분입니다.',
        '글리세린': '글리세린은 보습제 역할을 하며 피부를 부드럽게 유지하는 데 도움을 줍니다.',
        '프로필렌 글라이콜': '프로필렌 글라이콜은 피부의 수분을 유지시키는 데 도움을 줍니다.',
        '1(3)-부틸렌 글라이콘': '1(3)-부틸렌 글라이콘은 피부에 수분을 공급해주는 성분입니다.',
        '소디움PCA': '소디움PCA는 천연 보습 인자로 피부에 수분을 유지시킵니다.',
        '비타민E': '비타민E는 항산화 효과가 있으며 피부 손상을 방지하는 역할을 합니다.',
        '비타민A': '비타민A는 피부 재생을 촉진하고 피부 톤을 개선하는 데 도움을 줍니다.',
        '비타민C': '비타민C는 피부를 밝게 하고 콜라겐 생성을 촉진합니다.',
        '콜레젠': '콜레젠은 피부에 탄력을 주고 주름 예방에 도움을 줍니다.',
        '아보카도 오일': '아보카도 오일은 보습 효과가 뛰어나고 피부를 보호하는 역할을 합니다.',
        '이브닝 프라임 로즈 오일': '이브닝 프라임 로즈 오일은 피부 보습과 탄력 개선에 도움이 됩니다.',
        '오트밀 단백질': '오트밀 단백질은 피부를 진정시키고 보습하는 데 도움을 줍니다.',
        '콩 추출물': '콩 추출물은 피부를 부드럽게 하고 보습을 돕는 성분입니다.',
        '카모마일': '카모마일은 피부를 진정시키고 자극을 완화시키는 데 효과적입니다.',
        '오이': '오이는 피부에 수분을 공급하고 진정 효과가 뛰어납니다.',
        '복숭아': '복숭아는 피부를 촉촉하게 유지하고 피부 톤을 개선합니다.',
        '해조 추출물': '해조 추출물은 피부에 영양을 공급하고 보습 효과가 뛰어납니다.',
        '상백피 추출물': '상백피 추출물은 미백 효과가 있으며 피부 톤 개선에 도움을 줍니다.',
        '코직산': '코직산은 피부 미백에 도움을 주는 성분입니다.',
        '알부틴': '알부틴은 멜라닌 생성을 억제해 피부 톤을 밝게 해줍니다.',
        '포도씨 추출물': '포도씨 추출물은 항산화 효과가 있으며 피부 노화 방지에 도움이 됩니다.',
        '베타카로틴': '베타카로틴은 항산화 효과가 있어 피부를 보호합니다.',
        '시어버터': '시어버터는 보습 효과가 뛰어나고 피부를 부드럽게 만듭니다.',
        '파일워트 추출물': '파일워트 추출물은 피부 진정 효과가 있으며 피부 트러블 완화에 좋습니다.',
        '비타민B 복합체': '비타민B 복합체는 피부 보습과 재생에 효과적입니다.',
        '판테놀': '판테놀은 피부 보습 및 진정 효과가 있어 민감성 피부에 좋습니다.',

        // 건성 좋지 않은 성분
        '트리글리세라이드': '트리글리세라이드는 유분을 포함해 모공을 막을 수 있어 주의가 필요합니다.',
        '팔마티산염': '팔마티산염은 피부에 자극을 줄 수 있는 성분입니다.',
        '미리스틴산': '미리스틴산은 피부를 자극할 수 있어 주의가 필요합니다.',
        '스테아르산염': '스테아르산염은 건성 피부에는 자극이 될 수 있는 성분입니다.',
        '스테아린산': '스테아린산은 모공을 막을 수 있어 트러블을 유발할 수 있습니다.',
        '코코넛오일': '코코넛오일은 모공을 막아 여드름을 유발할 수 있습니다.',
        '시어버터': '시어버터는 일부 피부 타입에서는 트러블을 유발할 수 있습니다.',
        '바세린': '바세린은 피부에 오랫동안 남아 모공을 막을 수 있습니다.',
        '옥시벤존': '옥시벤존은 자외선 차단제에서 사용되며, 피부 자극을 유발할 수 있습니다.',
        '메톡시시나메이트': '메톡시시나메이트는 피부 자극을 유발할 수 있어 주의가 필요합니다.',

        // 지성 좋은 성분
        '글리콜린산': '글리콜린산은 각질 제거에 효과적이며 피부 재생을 촉진하는 성분입니다.',
        '살리실산': '살리실산은 모공 속 각질을 제거하여 피부를 깨끗하게 유지하는 데 도움을 줍니다.',
        '난 옥시놀-9': '난 옥시놀-9은 피부 세정 효과가 뛰어나며 보습 기능도 가지고 있습니다.',
        '녹차': '녹차는 항산화 효과가 뛰어나며 피부를 진정시키는 데 효과적입니다.',
        '위치 하젤': '위치 하젤은 피부 진정과 모공 수축 효과가 있어 여드름 피부에 좋습니다.',
        '레몬': '레몬은 비타민 C가 풍부하여 피부를 밝게 하고 탄력을 줍니다.',
        '캄파': '캄파는 피부 진정 효과가 있으며, 피부 자극을 완화시키는 데 도움을 줍니다.',
        '멘톨클로로필': '멘톨클로로필은 시원한 감각을 주어 피부를 상쾌하게 해줍니다.',
        '알라토인': '알라토인은 피부 진정과 보습에 탁월한 효과를 가지고 있습니다.',
        '티트리': '티트리는 항균 효과가 있어 여드름 등 피부 트러블 개선에 도움이 됩니다.',
        '감초': '감초는 피부 미백에 효과적이며 자극을 완화하는 데 도움을 줍니다.',
        '징크 옥사이트': '징크 옥사이트는 자외선 차단제로 사용되며, 피부를 보호해 줍니다.',
        '칼렌 듈라 추출물': '칼렌듈라 추출물은 피부 진정 효과가 뛰어나고 트러블 완화에 좋습니다.',
        '설퍼': '설퍼는 여드름 개선에 도움을 주며, 피지 조절 효과가 있습니다.',
        '트리클로잔': '트리클로잔은 항균 성분으로 여드름 등의 피부 트러블 완화에 좋습니다.',
        '티타늄 옥사이드': '티타늄 옥사이드는 자외선 차단제로 사용되며, 피부를 보호합니다.',

        // 지성 좋지 않은 성분
        '트리글리세라이드': '트리글리세라이드는 유분이 많아 모공을 막을 수 있어 트러블을 유발할 수 있습니다.',
        '진흙': '진흙은 흡착력은 뛰어나지만 건조한 피부에는 자극이 될 수 있습니다.',
        '계면 활성제': '계면 활성제는 세정 효과가 있지만 피부 장벽을 손상시킬 수 있어 주의가 필요합니다.',
        '멘톨': '멘톨은 피부에 시원함을 주지만 자극이 될 수 있어 민감성 피부에는 좋지 않습니다.',
        '페파민트': '페파민트는 피부에 자극을 줄 수 있어 민감성 피부에는 권장되지 않습니다.'

    };

    const descriptionPopup = document.getElementById('description-popup');
    const popupContent = document.getElementById('popup-content');

    window.showDescription = function(ingredient, event) {
        event.stopPropagation(); // 클릭 이벤트 전파 방지

        const description = ingredientDescriptions[ingredient] || '설명이 준비되지 않은 성분입니다.';

        if (popupContent && descriptionPopup) {
            popupContent.textContent = `${ingredient}: ${description}`;

            // 팝업이 닫혀 있는 경우에만 열기
            if (descriptionPopup.style.display === 'none' || descriptionPopup.style.display === '') {
                descriptionPopup.style.display = 'block';
                descriptionPopup.classList.remove('fade-out');
                descriptionPopup.classList.add('fade-in');
            }

            // 설명창의 너비를 설명에 맞게 조정
            const initialWidth = descriptionPopup.offsetWidth;
            descriptionPopup.style.width = 'auto';
            const newWidth = descriptionPopup.scrollWidth;
            descriptionPopup.style.width = `${initialWidth}px`;

            setTimeout(() => {
                descriptionPopup.style.width = `${newWidth}px`;
            }, 10);
        }
    };

    // 팝업 외부를 클릭했을 때 팝업을 닫기 위한 이벤트 리스너 추가
    document.addEventListener('mousedown', (event) => {
        if (
            descriptionPopup &&
            descriptionPopup.style.display === 'block' &&
            !descriptionPopup.contains(event.target) &&
            !event.target.classList.contains('ingredient-item')
        ) {
            closePopup();
        }
    });

    window.closePopup = function() {
        if (descriptionPopup) {
            descriptionPopup.classList.remove('fade-in');
            descriptionPopup.classList.add('fade-out');

            // fade-out 애니메이션이 끝난 후 팝업을 닫음
            setTimeout(() => {
                descriptionPopup.style.display = 'none';
                descriptionPopup.style.width = 'auto';
                descriptionPopup.classList.remove('fade-out');
            }, 500);
        }
    };
});


// 리모컨 메뉴
$(document).ready(function() {
    // 메뉴 클릭 시 스크롤 이동
    $('.menu li a').click(function(e) {
        e.preventDefault();
        var target = $(this).attr('href');
        var targetOffset = $(target).offset().top - 160; // 오프셋 조정
        $('html, body').animate({scrollTop: targetOffset}, 300);
    });

    // 스크롤 시 메뉴 상태 업데이트
    function updateMenuState() {
        var scrollTop = $(window).scrollTop();
        var activeFound = false; // 활성화된 메뉴를 찾았는지 확인하는 플래그

        // 각 섹션을 탐색하여 현재 위치 파악
        $('.result-container > div, h2').each(function() {
            var section = $(this);
            var sectionTop = section.offset().top - 170; // 오프셋 조정
            var sectionBottom = sectionTop + section.outerHeight(); // 섹션의 하단 위치 계산

            if (scrollTop >= sectionTop && scrollTop < sectionBottom) {
                var id = section.attr('id');
                $('.menu li').removeClass('active'); // 기존 활성화 제거
                $('.menu li a[href="#' + id + '"]').parent().addClass('active'); // 현재 위치 활성화
                activeFound = true; // 활성화된 메뉴를 찾았음
                return false; // 현재 섹션이 활성화된 상태를 유지
            }
        });

        // 활성화된 메뉴가 없는 경우 초기화
        if (!activeFound) {
            $('.menu li').removeClass('active');
        }
    }

    // 초기 상태 업데이트 및 스크롤, 리사이즈 시 업데이트
    $(window).on('scroll', updateMenuState);
    $(window).on('resize', updateMenuState);
    updateMenuState();

    // Up / Down 버튼 기능
    $('#btn-up').click(function() {
        $('html, body').animate({scrollTop: 0}, 450); // 페이지 최상단으로 이동
    });

    $('#btn-down').click(function() {
        $('html, body').animate({scrollTop: $(document).height()}, 550); // 페이지 최하단으로 이동
    });
});
