document.addEventListener("DOMContentLoaded", function () {

    const commentButton = document.getElementById("submitComment");
    const refreshButton = document.getElementById("refreshComment");
    const commentCountsElement = document.getElementById("commentsCount");
    const commentListContainer = document.getElementById("commentListContainer");

    const postId = document.getElementById("postId").value;
    const userId = null;
    const parentId = null;

    updateComments();

    function updateComments() {
        // 댓글 조회
        fetch('/posts/' + postId + '/comments')
            .then(response => {
                if(!response.ok) {
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

                    const commentElement = createCommentElement(comment);
                    commentContainer.appendChild(commentElement);

                    comment.replies.forEach(reply => {

                        const replyElement = createReplyElement(commentContainer, reply);
                        commentContainer.appendChild(replyElement);
                    });

                    commentListContainer.appendChild(commentContainer);
                });
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function createCommentElement(comment) {
        const template = document.getElementById('comment-template');
        const clone = template.content.cloneNode(true);

        clone.querySelector('.username').textContent = comment.username;
        clone.querySelector('.content').textContent = comment.content;

        // 삭제 버튼 바인딩
        const deleteBtn = clone.querySelector('.delete-btn');
        deleteBtn.setAttribute('data-comment-id', comment.id);
        deleteBtn.addEventListener('click', () => {
                console.log(`삭제 버튼 클릭 ${comment.id}`);
                deleteComment(comment.id);
            });

        return clone;
    }

    function createReplyElement(commentContainer, reply) {
        const template = document.getElementById('reply-template');
        const clone = template.content.cloneNode(true);

        clone.querySelector('.username').textContent = reply.username;
        clone.querySelector('.content').textContent = reply.content;

        // 삭제 버튼 바인딩
        const deleteBtn = clone.querySelector('.delete-btn');
        deleteBtn.setAttribute('data-comment-id', reply.id);
        deleteBtn.addEventListener('click', () => {
                console.log(`삭제 버튼 클릭 ${reply.id}`);
                deleteComment(comment.id);
            });

        return clone;
    }

    // 댓글 새로고침 버튼 클릭 바인딩
    refreshButton.addEventListener("click", function () {
        // console.log('새로고침');
        updateComments();
    });

    // 댓글 삭제 버튼 클릭 이벤트
    function deleteComment(commentId) {
            if (!confirm('댓글을 삭제하시겠습니까?')) {
                    return;
                }

            fetch('/posts/' + postId + '/comments/' + commentId, {
                    method: 'DELETE'
                })
                .then(response => {
                    if(!response.ok) {
                        throw new Error('댓글 삭제 실패');
                    }

                    updateComments();
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }


    // 비로그인회원
    if(commentButton == null) {
       return;
    }

    // 댓글 작성 버튼 클릭 바인딩
    commentButton.addEventListener("click", function () {
            //const parentId = document.getElementById("parentId").value;

            const commentText = document.getElementById("comment").value.trim();
            const userId = document.getElementById("userId").value;

            if (commentText === "") {
                alert("댓글을 입력하세요!");
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
                          'Content-Type': 'application/json'
                      },
                      body: JSON.stringify(data)
                  })
                  .then(response => {

                      if (!response.ok) {
                          throw new Error('Request failed');
                      }

                      document.getElementById("comment").value = "";
                      updateComments();
                  })
                  .catch(error => {
                      console.error('Error:', error);
                  });
        });

});