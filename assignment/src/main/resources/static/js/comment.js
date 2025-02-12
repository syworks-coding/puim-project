document.addEventListener("DOMContentLoaded", function () {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    const commentButton = document.getElementById("submitComment");
    const refreshButton = document.getElementById("refreshComment");
    const commentCountsElement = document.getElementById("commentsCount");
    const commentListContainer = document.getElementById("commentListContainer");

    const postId = document.getElementById("postId").value;
    const parentId = null;

    updateComments();

    function updateComments() {
        let userId = null;
        if(commentButton != null) {
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

                    comment.replies.forEach(reply => {

                        const replyElement = createReplyElement(commentContainer, reply, userId);
                        commentContainer.appendChild(replyElement);

                    });

                    commentListContainer.appendChild(commentContainer);
                });
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function createCommentElement(comment, userId) {
        const template = document.getElementById('comment-template');
        const clone = template.content.cloneNode(true);

        const replyInputContainer = clone.querySelector('.replyInputContainer');

        const commentContainer = clone.querySelector('.comment-container');
        commentContainer.id = `comment-${comment.id}`;

        clone.querySelector('.username').textContent = comment.username;
        clone.querySelector('.content').textContent = comment.content;

        clone.querySelector('.createdAt').textContent = comment.createdAt;
        if(comment.updatedAt != null && new Date(comment.createdAt) < new Date(comment.updatedAt)) {
            clone.querySelector('.updatedAt').textContent = ' / ' + comment.updatedAt + ' 수정';
        }

        // 삭제 버튼 바인딩
        const deleteBtn = clone.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', () => {
            deleteComment(comment.id);
        });

        // 수정 버튼 바인딩
        const editBtn = clone.querySelector('.edit-btn');
        editBtn.setAttribute('data-comment-id', comment.id);
        editBtn.addEventListener('click', () => {
            editComment(comment);
        });

        // 답글 버튼 바인딩
        const replyBtn = clone.querySelector('.reply-btn');
        replyBtn.addEventListener('click', () => {
            replyButtonClicked(replyInputContainer, comment.id);
        });

        clone.querySelector(".commentEdit").hidden = comment.userId !== userId;

        return clone;
    }

    // 답글 조회용
    function createReplyElement(commentContainer, reply, userId) {
        const template = document.getElementById('reply-template');
        const clone = template.content.cloneNode(true);

        clone.querySelector('.username').textContent = reply.username;
        clone.querySelector('.content').textContent = reply.content;
        clone.querySelector('.createdAt').textContent = reply.createdAt;

        if(reply.updatedAt != null && new Date(reply.createdAt) < new Date(reply.updatedAt)) {
            clone.querySelector('.updatedAt').textContent = ' / ' + reply.updatedAt + ' 수정';
        }

        const replyContainer = clone.querySelector('.reply-container');
        replyContainer.id = `comment-${reply.id}`;

        // 삭제 버튼 바인딩
        const deleteBtn = clone.querySelector('.delete-btn');
        deleteBtn.setAttribute('data-comment-id', reply.id);
        deleteBtn.addEventListener('click', () => {
            deleteComment(reply.id);
        });

        // 수정 버튼 바인딩
        const editBtn = clone.querySelector('.edit-btn');
        editBtn.setAttribute('data-comment-id', reply.id);
        editBtn.addEventListener('click', () => {
            editComment(reply);
        });

        clone.querySelector(".replyEdit").hidden = reply.userId !== userId;

        return clone;
    }

    // 댓글 새로고침 버튼 클릭 바인딩
    refreshButton.addEventListener("click", function () {
        updateComments();
    });

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

    // 댓글 수정 버튼 클릭 이벤트
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
        editBtn.addEventListener("click", function() {
            patchComment(postId, comment.id, editTextElement.value.trim());
        });

        // 취소 버튼 바인딩
        const cancelEditBtn = editElement.querySelector('.cancelEditBtn');
        cancelEditBtn.addEventListener("click", function() {
            commentDiv.innerHTML = commentHTML;

            // 삭제 버튼 바인딩
            const deleteBtn = commentDiv.querySelector('.delete-btn');
            deleteBtn.addEventListener('click', () => {
                deleteComment(comment.id);
            });

            // 수정 버튼 바인딩
            const editBtn = commentDiv.querySelector('.edit-btn');
            editBtn.setAttribute('data-comment-id', comment.id);
            editBtn.addEventListener('click', () => {
                editComment(comment);
            });

            // 답글 버튼 바인딩
            const replyBtn = commentDiv.querySelector('.reply-btn');
            if(replyBtn != null) {
                replyBtn.addEventListener('click', () => {
                    replyButtonClicked(commentDiv.querySelector('.replyInputContainer'), comment.id);
                });
            }
        });

        commentDiv.appendChild(editElement);
    }

    function createEditCommentElement(comment) {
        const template = document.getElementById('comment-edit-template');
        const clone = template.content.cloneNode(true);

        clone.querySelector('.username').textContent = comment.username;
        clone.querySelector('.editContent').textContent = comment.content;

        return clone;
    }

    // 답글 버튼 클릭 이벤트
    function replyButtonClicked(replyContainer, commentId) {

        if (commentButton == null) {
            alert('회원 전용 기능입니다;');
            return;
        }

        const isVisible = replyContainer.hidden === false;
        const commentElement = replyContainer.querySelector(".reply");

        // 이미 답글 폼 열려잇으면 닫음
        if (isVisible) {
            replyContainer.hidden = true;
            replySubmitButton.removeEventListener("click", handleSubmit);
            return;
        }

        replyContainer.hidden = false;

        // 답글 등록 버튼 바인딩
        const userId = document.getElementById("userId").value;
        const replySubmitButton = replyContainer.querySelector(".submitReply");

        replySubmitButton.addEventListener("click", handleSubmit);

        function handleSubmit() {
            const commentText = commentElement.value.trim();
            postComment(postId, commentText, userId, commentId, () => {
                commentElement.value = "";
            });
        }
    }

    // 비로그인회원
    if (commentButton == null) {
        return;
    }

    // 댓글 등록 버튼 클릭 바인딩
    commentButton.addEventListener("click", function () {
        const commentText = document.getElementById("comment").value.trim();
        const userId = document.getElementById("userId").value;

        postComment(postId, commentText, userId, null, () => {
            document.getElementById("comment").value = "";
        });
    });

    function postComment(postId, commentText, userId, parentId, onUpdate) {

        if (commentText === "") {
            alert("댓글을 입력하세요!");
            return;
        }

        if (commentText.length > 200) {
            alert("최대 200자 까지 작성 가능합니다.");
            return;
        }

        console.log("작성된 댓글:", commentText);

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

    function patchComment(postId, commentId, content) {
        if (content === "") {
            alert("댓글을 입력하세요!");
            return;
        }

        if (commentText.length > 200) {
            alert("최대 200자 까지 작성 가능합니다.");
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
});