document.addEventListener('DOMContentLoaded', () => {
    // 성공 메시지가 존재하면 알림 표시
    const successMeta = document.querySelector('meta[name="successMessage"]');
    if (successMeta) {
        alert(successMeta.content); // 메시지를 알림창으로 출력
        window.location.href = '/'; // index.html로 리다이렉트
    }
});

document.addEventListener("DOMContentLoaded", function () {
    fetch("/cosmetics/my-page/liked-cosmetics")
        .then(response => {
            if (!response.ok) {
                throw new Error("찜 목록을 불러오는데 실패했습니다.");
            }
            return response.json();
        })
        .then(data => {
            const likedItemsContainer = document.getElementById("liked-items");
            likedItemsContainer.innerHTML = ""; // 기존 데이터 초기화

            data.forEach(item => {
                // 새로운 박스형 카드 생성
                const itemBox = document.createElement("div");
                itemBox.className = "liked-item-box";
                itemBox.innerHTML = `
                    <img src="${item.image_link}" alt="${item.product}" class="liked-item-image">
                    <p class="liked-item-name">${item.product.length > 20 ? item.product.substring(0, 20) + '...' : item.product}</p>
                    <div class="liked-item-brand-price">
                      <p class="liked-item-brand">${item.brand}</p>
                      <p class="liked-item-price">🪙 ${item.price_after}</p>
                    </div>
                    <button class="btn btn-danger unlike-btn" data-id="${item.id}">❌</button>
                `;

                // 박스 클릭 시 디테일 페이지로 이동
                itemBox.addEventListener("click", () => {
                    window.location.href = `/cosmetics/product-detail/${item.id}`;
                });

                // "좋아요 취소" 버튼 클릭 시
                const unlikeButton = itemBox.querySelector(".unlike-btn");
                unlikeButton.addEventListener("click", (event) => {
                    event.stopPropagation(); // 이벤트 전파 중단
                    const cosmeticsId = unlikeButton.dataset.id;

                    // 좋아요 취소 요청
                    fetch(`/cosmetics/toggle-cosmetics-great?cosmeticsId=${cosmeticsId}`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                    })
                        .then((response) => {
                            if (!response.ok) {
                                throw new Error("좋아요 취소에 실패했습니다.");
                            }
                            return response.json();
                        })
                        .then((data) => {
                            if (!data.liked) {
                                // 박스를 삭제
                                itemBox.remove();
                                alert("좋아요가 취소되었습니다.");
                            }
                        })
                        .catch((error) => {
                            console.error("좋아요 취소 중 에러:", error);
                        });
                });

                likedItemsContainer.appendChild(itemBox);
            });
        })
        .catch(error => {
            console.error("찜 목록 로드 중 에러:", error);
        });
});


// 내가 작성한 글 목록을 불러오는 함수 (부트스트랩 탭의 숨김 기능 이용하기 위해 innerHTML 사용)
document.addEventListener("DOMContentLoaded", function () {
    const mySelfBoardTab = document.getElementById("my-self-board-tab");

    mySelfBoardTab.addEventListener("click", function () {
        fetch("/my-page/my-boards")
            .then((response) => response.json())
            .then((data) => {
                const boardContainer = document.getElementById("my-boards");
                boardContainer.innerHTML = ""; // 기존 데이터를 초기화

                data.forEach((board) => {
                    const boardBox = document.createElement("div");
                    boardBox.className = "board-box";

                    // 제목 길이 제한
                    const truncatedSubject =
                        board.subject.length > 20
                            ? board.subject.substring(0, 20) + "..."
                            : board.subject;

                    boardBox.innerHTML = `
            <div class="board-info">
              <span class="board-subject">📌 ${truncatedSubject}</span>
              <span class="board-date">작성일: ${board.date}</span>
              <span class="board-view">조회수: ${board.view}</span>
              <span class="board-reactions">
                ❤️${board.great} 💬${board.commentCount}
              </span>
            </div>
          `;

                    boardBox.onclick = () => {
                        window.location.href = `/board/board-detail/${board.id}`;
                    };
                    boardContainer.appendChild(boardBox);
                });
            })
            .catch((error) => console.error("Error fetching boards:", error));
    });
});


// 내가 댓글 단 글 불러오는 함수
document.addEventListener("DOMContentLoaded", function () {
    const mySelfReplyTab = document.getElementById("my-self-reply-tab");

    mySelfReplyTab.addEventListener("click", function () {
        fetch("/my-page/my-replies")
            .then((response) => response.json())
            .then((data) => {
                const container = document.getElementById("my-replies-container");
                container.innerHTML = ""; // 기존 데이터를 초기화

                data.forEach((reply) => {
                    const replyBox = document.createElement("div");
                    replyBox.className = "reply-box";

                    // 제목 글자수 제한
                    const truncatedSubject =
                        reply.subject.length > 15
                            ? reply.subject.substring(0, 15) + "..."
                            : reply.subject;

                    // 댓글 내용 글자수 제한
                    const truncatedContent =
                        reply.content.length > 50
                            ? reply.content.substring(0, 50) + "..."
                            : reply.content;

                    replyBox.innerHTML = `
            <div class="reply-header">
              <span class="reply-type">${reply.type === "comment" ? "댓글" : "대댓글"}</span>
              <span class="reply-subject">📌 ${reply.subject} 📌</span>
              <span class="reply-date">${reply.date}</span>
            </div>
            <div class="reply-content">
              <p>${truncatedContent}</p>
            </div>
          `;

                    // 박스 클릭 시 해당 게시글로 이동
                    replyBox.addEventListener("click", function () {
                        window.location.href = `/board/board-detail/${reply.boardId}`;
                    });

                    container.appendChild(replyBox);
                });
            })
            .catch((error) => console.error("댓글 로드 중 에러:", error));
    });
});


// 내가 좋아요 누른 글 불러오는 함수
document.addEventListener("DOMContentLoaded", function () {
    const mySelfBoardGreatTab = document.getElementById("my-self-board-great-tab");

    mySelfBoardGreatTab.addEventListener("click", function () {
        fetch("/board/liked")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("좋아요 누른 글을 가져오는 데 실패했습니다.");
                }
                return response.json();
            })
            .then((data) => {
                const boardContainer = document.getElementById("my-likes");
                boardContainer.innerHTML = ""; // 기존 데이터를 초기화

                data.forEach((board) => {
                    const boardBox = document.createElement("div");
                    boardBox.className = "board-box";

                    // 제목 길이 제한
                    const truncatedSubject =
                        board.subject.length > 20
                            ? board.subject.substring(0, 20) + "..."
                            : board.subject;

                    boardBox.innerHTML = `
            <div class="board-info">
              <span class="board-subject">📌 ${truncatedSubject}</span>
              <span class="board-writer">작성자: ${board.writer}</span>
              <span class="board-date">작성일: ${board.date}</span>
              <span class="board-view">조회수: ${board.view}</span>
              <span class="board-reactions">
                ❤️${board.great} 💬${board.commentCount}
              </span>
            </div>
          `;

                    boardBox.onclick = () => {
                        window.location.href = `/board/board-detail/${board.id}`;
                    };
                    boardContainer.appendChild(boardBox);
                });
            })
            .catch((error) => console.error("좋아요 누른 글 로드 중 오류:", error));
    });
});


