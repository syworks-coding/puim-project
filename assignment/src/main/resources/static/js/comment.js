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

                    const commentElement = document.createElement('div');
                    commentElement.classList.add('card', 'p-1', 'mb-4');

                    commentElement.innerHTML = `
                        <div class="card-body">
                        <div class="row">
                            <div class="col-8 text-start">
                                <b>${comment.username}</b>
                            </div>
                            <div class="col-4 text-end">
                                <button type="button" class="btn btn-link">수정</button>
                                <button type="button" class="btn btn-link" data-comment-id="${comment.id}">삭제</button>
                            </div>
                        </div>
                            <p class="card-text">${comment.content}</p>
                            <button class="btn btn-sm btn-outline-primary reply-btn">답글</button>
                        </div>
                    `;



                    commentContainer.appendChild(commentElement);

                    comment.replies.forEach(reply => {
                        const replyContainer = document.createElement('div');
                        replyContainer.classList.add('ms-4', 'border-start', 'ps-3');

                        const replyElement = document.createElement('div');
                        replyElement.classList.add('card', 'p-1', 'mb-2');

                        replyElement.innerHTML = `
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-8 text-start">
                                            <b>${reply.username}</b>
                                        </div>
                                        <div class="col-4 text-end">
                                            <button type="button" class="btn btn-link">수정</button>
                                            <button type="button" class="btn btn-link">삭제</button>
                                        </div>
                                    </div>
                                    <p class="card-text">${reply.content}</p>
                                </div>
                        `;

                        replyContainer.appendChild(replyElement);
                        commentContainer.appendChild(replyContainer);
                    });

                    commentListContainer.appendChild(commentContainer);
                });
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // 댓글 새로고침 버튼 클릭 바인딩
    refreshButton.addEventListener("click", function () {
        // console.log('새로고침');
        updateComments();
    });

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


    // 댓글 삭제 버튼 클릭 이벤트
    function deleteComment() {

    }

});