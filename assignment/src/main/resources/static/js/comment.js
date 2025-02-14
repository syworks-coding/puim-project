document.addEventListener("DOMContentLoaded", function () {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    const commentInputElement = document.getElementById("comment");
    const commentLengthElement = document.getElementById("commentLength");
    const commentButton = document.getElementById("submitComment");

    const refreshButton = document.getElementById("refreshComment");
    const commentCountsElement = document.getElementById("commentsCount");

    const postId = document.getElementById("postId").value;

    const maxContentLength = 50;

    // ~ 함수 정의  ~

    function postComment(maxContentLength, postId, commentText, userId, parentId, onUpdate) {

        if (commentText === "") {
            alert("댓글을 입력하세요!");
            return;
        }

        if (commentText.length > maxContentLength) {
            alert("최대 " + maxContentLength + "자 까지 작성 가능합니다.");
            return;
        }

        const url = '/posts/' + postId + '/comments';  // POST 요청을 보낼 URL
        const data = {
            postId: postId,
            content: commentText,
            userId: userId,
            parentId: parentId
        };
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Request failed');
                }
                onUpdate();
                updateComments();
                return true;
            })
            .catch(error => {
                console.error('Error:', error);
                return false;
            });
    }

    function patchComment(maxContentLength, postId, commentId, content) {
        if (content === "") {
            alert("댓글을 입력하세요!");
            return;
        }

        if (content.length > maxContentLength) {
            alert("최대 " + maxContentLength + "자 까지 작성 가능합니다.");
            return;
        }

        const url = '/posts/' + postId + '/comments/' + commentId;
        const data = {
            content: content,
        };
        fetch(url, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Request failed');
                }
                updateComments();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function updateComments() {
        let userId = null;
        if (commentButton != null) {
            userId = Number(document.getElementById("userId").value);
        }

        // 댓글 조회
        fetch('/posts/' + postId + '/comments')
            .then(response => {
                if (!response.ok) {
                    throw new Error('댓글 조회 실패');
                }
                return response.json();
            })
            .then(data => {
                // 댓글 수 업데이트
                commentCountsElement.innerText = data.totalCount;

                // 댓글을 화면에 동적으로 추가
                const commentList = data.comments;
                const commentListContainer = document.getElementById("commentListContainer");
                commentListContainer.innerHTML = '';  // 기존 댓글 목록 초기화

                commentList.forEach(comment => {

                    const commentContainer = document.createElement('div');

                    const commentElement = createCommentElement(comment, userId);
                    commentContainer.appendChild(commentElement);
                    initRenderLikes(commentContainer, comment.id)

                    comment.replies.forEach(reply => {

                        const replyElement = createReplyElement(commentContainer, reply, userId);
                        initRenderLikes(replyElement, reply.id);
                        commentContainer.appendChild(replyElement);
                    });
                    commentListContainer.appendChild(commentContainer);
                });
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function getLikes(commentId, onDataLoaded) {
        fetch('/comments/' + commentId + '/likes')
            .then(response => {
                if (!response.ok) {
                    throw new Error('좋아요 조회 실패');
                }
                return response.json();
            })
            .then(data => {
                onDataLoaded(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function initRenderLikes(clone, commentId) {
        const likesCount = clone.querySelector('.comment-likes');
        const likesIcon = clone.querySelector(".likesIcon");

        getLikes(commentId, function (data) {
            likesCount.innerText = data.likeCount;

            likesIcon.classList.remove('bi-heart-fill', 'bi-heart');
            const likesClass = data.liked === true ? 'bi-heart-fill' : 'bi-heart';
            likesIcon.classList.add(likesClass);
        });
    }

    // 글자 수 표시 및 제한
    function renderContentLength(inputElement, lengthElement, maxContentLength) {
        const length = inputElement.value.length;
        if (length >= maxContentLength) {
            const origin = inputElement.value;
            inputElement.value = origin.slice(inputElement.value, maxContentLength);
        }

        lengthElement.textContent = Math.min(length, maxContentLength) + '/' + maxContentLength + '자';
    }

    // 댓글 삭제 버튼 클릭 이벤트
    function deleteComment(commentId) {
        if (!confirm('댓글을 삭제하시겠습니까?')) {
            return;
        }

        fetch('/posts/' + postId + '/comments/' + commentId, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': csrfToken
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('댓글 삭제 실패');
                }

                updateComments();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // 답글 버튼 클릭 이벤트
    function replyButtonClicked(replyContainer) {

        if (commentButton == null) {
            alert('회원 전용 기능입니다;');
            return;
        }

        const isVisible = replyContainer.hidden === false;

        // 이미 답글 폼 열려잇으면 닫음
        if (isVisible) {
            replyContainer.querySelector(".reply").value = "";
            replyContainer.hidden = true;
            return;
        }

        replyContainer.hidden = false;
    }

    // 좋아요 버튼 클릭 이벤트
    function likesComment(commentId, onUpdate) {
        if (commentButton == null) {
            alert('회원 전용 기능입니다.');
            return;
        }

        const url = '/comments/' + commentId + '/likes';

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Request failed');
                }

                onUpdate();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function bindCommentEvent(clone, commentContainer, comment, userId) {
        commentContainer.id = `comment-${comment.id}`;

        clone.querySelector('.username').textContent = comment.username;
        clone.querySelector('.content').textContent = comment.content;

        clone.querySelector('.createdAt').textContent = comment.createdAt;
        if (comment.updatedAt != null && new Date(comment.createdAt) < new Date(comment.updatedAt)) {
            clone.querySelector('.updatedAt').textContent = ' / ' + comment.updatedAt + ' 수정';
        }

        // 삭제 버튼 바인딩
        const deleteBtn = clone.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', () => {
            deleteComment(comment.id);
        });

        // 수정 버튼 바인딩
        const editBtn = clone.querySelector('.edit-btn');
        editBtn.addEventListener('click', () => {
            editComment(comment);
        });

        // 좋아요 버튼 바인딩
        const likesBtn = clone.querySelector('.likes-btn');
        const likesCount = clone.querySelector('.comment-likes');
        const likesIcon = clone.querySelector(".likesIcon");

        likesBtn.addEventListener('click', () => {
            likesComment(comment.id, () => getLikes(comment.id, function(data) {
                likesCount.innerText = data.likeCount;

                likesIcon.classList.remove('bi-heart-fill', 'bi-heart');
                const likesClass = data.liked === true ? 'bi-heart-fill' : 'bi-heart';
                likesIcon.classList.add(likesClass);
            }));
        });

        clone.querySelector(".commentEdit").hidden = comment.userId !== userId;
    }

    // 댓글 HTML 생성
    function createCommentElement(comment, userId) {
        const template = document.getElementById('comment-template');
        const clone = template.content.cloneNode(true);

        const replyInputContainer = clone.querySelector('.replyInputContainer');

        const commentContainer = clone.querySelector('.comment-container');
        bindCommentEvent(clone, commentContainer, comment, userId);

        // 답글 글자수 인풋 바인딩
        const replyElement = replyInputContainer.querySelector(".reply");
        const replyLengthElement = replyInputContainer.querySelector(".replyLength");
        replyElement.addEventListener("input", () => renderContentLength(replyElement, replyLengthElement, maxContentLength));

        // 답글 버튼 바인딩
        const replyBtn = clone.querySelector('.reply-btn');
        replyBtn.addEventListener('click', () => {
            renderContentLength(replyElement, replyLengthElement, maxContentLength);
            replyButtonClicked(replyInputContainer);
        });

        // 답글 등록 버튼 바인딩
        const replySubmitButton = replyInputContainer.querySelector(".submitReply");
        replySubmitButton.addEventListener("click", () => {
            const commentText = replyElement.value.trim();
            postComment(maxContentLength, postId, commentText, userId, comment.id, () => {
                replyElement.value = "";
            });
        });

        return clone;
    }

    // 답글 HTML 생성
    function createReplyElement(commentContainer, reply, userId) {
        const template = document.getElementById('reply-template');
        const clone = template.content.cloneNode(true);

        const replyContainer = clone.querySelector('.reply-container');
        bindCommentEvent(clone, replyContainer, reply, userId);

        return clone;
    }

    // 댓글(답글) 수정 HTML 생성
    function createEditCommentElement(comment) {
        const template = document.getElementById('comment-edit-template');
        const clone = template.content.cloneNode(true);

        clone.querySelector('.username').textContent = comment.username;
        clone.querySelector('.editContent').textContent = comment.content;

        return clone;
    }

    // 댓글 수정 버튼 클릭 이벤트(댓글(답글) 수정 HTML 버튼 이벤트 바인딩)
    function editComment(comment) {

        // 해당 댓글 입력 폼으로 바뀜
        const commentDiv = document.getElementById(`comment-${comment.id}`);

        const commentHTML = commentDiv.innerHTML;

        // 수정 폼으로 변경
        commentDiv.innerHTML = ``;
        const editElement = createEditCommentElement(comment);

        // 수정 버튼 바인딩
        const editBtn = editElement.querySelector('.editCommentBtn');
        const editTextElement = editElement.querySelector('.editContent');
        editBtn.addEventListener("click", function () {
            patchComment(maxContentLength, postId, comment.id, editTextElement.value.trim());
        });

        // 글자수
        const editLengthElement = editElement.querySelector('.editLength');
        renderContentLength(editTextElement, editLengthElement, maxContentLength);
        editTextElement.addEventListener("input", () => renderContentLength(editTextElement, editLengthElement, maxContentLength));

        // 취소 버튼 바인딩
        const cancelEditBtn = editElement.querySelector('.cancelEditBtn');
        cancelEditBtn.addEventListener("click", function () {
            commentDiv.innerHTML = commentHTML;
            const userId = Number(document.getElementById("userId").value);

            bindCommentEvent(commentDiv, commentDiv, comment, userId);

            // 답글 버튼 바인딩
            const replyBtn = commentDiv.querySelector('.reply-btn');
            if (replyBtn != null) {
                replyBtn.addEventListener('click', () => {
                    replyButtonClicked(commentDiv.querySelector('.replyInputContainer'));
                });

                // 답글 등록 버튼 바인딩
                const replySubmitButton = commentDiv.querySelector(".submitReply");
                const commentElement = commentDiv.querySelector(".reply");
                replySubmitButton.addEventListener("click", () => {
                    const commentText = commentElement.value.trim();
                    postComment(maxContentLength, postId, commentText, userId, comment.id, () => {
                        commentElement.value = "";

                    });
                });
            }
        });

        commentDiv.appendChild(editElement);
    }

    // ~ 실제 실행 부분 ~

    updateComments();

    // 댓글 새로고침 버튼 클릭 바인딩
    refreshButton.addEventListener("click", function () {
        updateComments();
    });

    // 비로그인회원
    if (commentButton == null) {
        return;
    }

    renderContentLength(commentInputElement, commentLengthElement, maxContentLength);

    // 댓글 글자수 인풋 바인딩
    commentInputElement.addEventListener("input",
        () => renderContentLength(commentInputElement, commentLengthElement, maxContentLength));

    // 댓글 등록 버튼 클릭 바인딩
    commentButton.addEventListener("click", function () {
        const commentText = commentInputElement.value.trim();
        const userId = document.getElementById("userId").value;

        postComment(maxContentLength, postId, commentText, userId, null, () => {
            document.getElementById("comment").value = "";
            renderContentLength(commentInputElement, commentLengthElement, maxContentLength);
        });
    });
});