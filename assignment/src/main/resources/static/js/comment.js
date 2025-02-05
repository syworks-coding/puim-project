document.addEventListener("DOMContentLoaded", function () {

    const commentButton = document.getElementById("submitComment");
    const commentCountsElement = document.getElementById("commentsCount");
    const commentListContainer = document.getElementById("commentListContainer");

    const postId = document.getElementById("postId").value;
    const userId = null;
    const parentId = null;

    // 댓글 조회
    fetch('/posts/' + postId + '/comments')
    .then(response => {
        if(!response.ok) {
            throw new Error('댓글 조회 실패');
        }
        return response.json();
    })
    .then(commentList => {
        // 댓글 수 업데이트
        commentCountsElement.innerText = commentList.length;

        // 댓글을 화면에 동적으로 추가
        const commentListContainer = document.getElementById("commentListContainer");
        commentListContainer.innerHTML = '';  // 기존 댓글 목록 초기화

        commentList.forEach(comment => {
            const commentElement = document.createElement('div');
            commentElement.classList.add('card', 'p-1', 'mb-4');

            commentElement.innerHTML = `
                <div class="card-body">
                    <p class="card-text"><b>${comment.username}</b></p>
                    <p class="card-text">${comment.content}</p>
                    <button class="btn btn-sm btn-outline-primary reply-btn">답글</button>
                </div>
            `;

            commentListContainer.appendChild(commentElement);
        });
    })
    .catch(error => {
        console.error('Error:', error);
    });


    if(commentButton == null) {
       return;
    }

    //댓글작성 버튼 클릭 바인딩
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

                       console.log(url);
                       console.log(data);

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
                       })
                       .catch(error => {
                           console.error('Error:', error);
                       });

        });

});