// // 댓글 작성 기능
function addComment(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new URLSearchParams(new FormData(form));

    fetch(`/comments/comment`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('댓글 작성에 실패했습니다.');
            }
            return response.text();
        })
        .then(message => {
            alert(message); // 댓글 작성 완료 메시지 표시

            // 댓글 수 업데이트
            const commentCountElement = document.getElementById("commentCount");
            if (commentCountElement) { // 요소가 있는지 확인
                const currentCount = parseInt(commentCountElement.innerText) || 0;
                updateCommentCount(currentCount + 1);
            } else {
                console.warn('commentCount 요소를 찾을 수 없습니다.');
            }

            location.reload(); // 새로고침
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });
}



// 대댓글 작성 기능
function addReplyComment(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    const boardId = formData.get("boardId");
    const author = formData.get("author");
    const content = formData.get("content");
    const commentId = formData.get("commentId");

    fetch(`/reply/replies`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
            boardId: boardId,
            content: content,
            author: author,
            commentId: commentId
        })
    })
        .then(response => {
            if (!response.ok) throw new Error('대댓글 작성에 실패했습니다.');
            return response.text();
        })
        .then(message => {
            alert(message);
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });
}

// 댓글 수 업데이트 함수
function updateCommentCount(newCount) {
    const commentCountElement = document.getElementById("commentCount");
    if (commentCountElement) { // 요소가 있는지 확인
        commentCountElement.innerText = newCount;
        commentCountElement.style.display = newCount > 0 ? "inline" : "none"; // 댓글 수가 1 이상일 때 표시
    }
}


/*
*  게시판, 댓글, 대댓글 삭제
* */

// 게시판 삭제
function deleteBoard(button) {
    const form = button.closest(".delete-board-form");

    // 폼 기본 동작 방지
    form.addEventListener("submit", function(event) {
        event.preventDefault();
    });

    const boardSubject = form.querySelector("input[name='boardSubject']").value;
    const boardId = form.querySelector("input[name='boardId']").value;

    // 사용자 확인을 위한 alert 추가
    if (!confirm(`게시글 "${boardSubject}"을(를) 정말 삭제하시겠습니까?`)) {
        return; // 사용자가 취소를 누르면 함수 종료
    }

    // 삭제 요청
    fetch(`/board/delete`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
            boardId: boardId,
            boardSubject: boardSubject
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('게시글 삭제에 실패했습니다.');
            }
            window.location.href = '/board'; // 성공 시 /board로 이동
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message || '게시글 삭제 중 오류가 발생했습니다.');
        });
}


// 댓글 삭제
function deleteComment(img) {
    const form = img.closest(".delete-comment-form");
    const commentId = form.querySelector("input[name='commentId']").value;
    if (!commentId) {
        alert("댓글 ID를 찾을 수 없습니다.");
        return;
    }

    fetch(`/comments/delete`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ comment_id: commentId })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('댓글 삭제에 실패했습니다.');
            }
            alert('댓글이 삭제되었습니다.');
            location.reload(); // 페이지 새로고침
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });
}

// 대댓글 삭제 기능
function deleteReply(img) {
    const form = img.closest(".delete-reply-form");
    const replyId = form.querySelector("input[name='replyId']").value;
    if (!replyId) {
        alert("댓글 ID를 찾을 수 없습니다.");
        return;
    }
    fetch(`/reply/delete`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ replyId: replyId })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('대댓글 삭제에 실패했습니다.');
            }
            alert('대댓글이 삭제되었습니다.');
            location.reload(); // 페이지 새로고침
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });
}



// 대댓글 작성 폼 숨기기
document.querySelectorAll(".replyButton").forEach((img) => {
    img.addEventListener("click", function () {
        const replyFormDiv = this.closest('.comment').querySelector(".replyForm-div");
        if (replyFormDiv) {
            replyFormDiv.style.display = replyFormDiv.style.display === "none" ? "block" : "none";
        }
    });
});


/*
*  게시판, 댓글, 대댓글 좋아요
* */

// 게시판 좋아요 버튼 클릭 이벤트
function toggleBoardGreat(img) {
    const form = img.closest(".board-great-form");
    const boardId = form.querySelector("input[name='boardId']").value;
    console.log("Board ID:", boardId); // 디버깅을 위한 출력

    fetch('/board/toggle-board-great', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ boardId: boardId })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log("Response data:", data); // 디버깅을 위한 출력

            // 이미지 변경 (캐시 무효화를 위해 타임스탬프 추가)
            const timestamp = new Date().getTime();
            img.src = data.liked ? `/image/great_af_icon.png?${timestamp}` : `/image/great_be_icon.png?${timestamp}`;

            // 좋아요 수 업데이트
            const greatCounter = document.getElementById("greatCount");
            if (greatCounter) {
                greatCounter.innerText = data.great;
                greatCounter.style.display = data.great > 0 ? "inline" : "none"; // 좋아요 수에 따라 표시/숨김
            }
        })
        .catch(error => {
            console.error('Fetch Error:', error);
            alert('좋아요 상태를 변경하는 도중 오류가 발생했습니다. ' + error);
        });
}



// Comment 좋아요 버튼 이벤트
function toggleCommentGreat(img) {
    const form = img.closest(".comment-great-form");
    const commentId = form.querySelector("input[name='commentId']").value;

    fetch('/comments/toggle-comment-great', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ commentId: commentId })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const timestamp = new Date().getTime();
            img.src = data.liked ? `/image/great_af_icon.png?${timestamp}` : `/image/great_be_icon.png?${timestamp}`;

            const greatCounter = form.closest('.comment').querySelector('.comment-great-counter');
            if (greatCounter) {
                greatCounter.innerText = data.great;
            }

            const commentGreatBox = form.closest('.comment').querySelector('.comment-great-box');
            if (commentGreatBox) {
                // 좋아요 수에 따라 표시/숨김
                commentGreatBox.style.display = data.great > 0 ? 'flex' : 'none';
            }
        })
        .catch(error => {
            console.error('Fetch Error:', error);
            alert('좋아요 상태를 변경하는 도중 오류가 발생했습니다. ' + error);
        });
}

// Reply 좋아요 버튼 이벤트
function toggleReplyGreat(img) {
    const form = img.closest(".reply-great-form");
    const replyId = form.querySelector("input[name='replyId']").value;

    fetch('/reply/toggle-reply-great', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ replyId: replyId })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const timestamp = new Date().getTime();
            img.src = data.liked ? `/image/great_af_icon.png?${timestamp}` : `/image/great_be_icon.png?${timestamp}`;

            const greatCounter = form.closest('.replyComment-list').querySelector('.reply-great-counter');
            if (greatCounter) {
                greatCounter.innerText = data.great;
            }

            const replyGreatBox = form.closest('.replyComment-list').querySelector('.reply-great-box');
            if (replyGreatBox) {
                // 좋아요 수에 따라 표시/숨김
                replyGreatBox.style.display = data.great > 0 ? 'flex' : 'none';
            }
        })
        .catch(error => {
            console.error('Fetch Error:', error);
            alert('좋아요 상태를 변경하는 도중 오류가 발생했습니다.' + error);
        });
}


