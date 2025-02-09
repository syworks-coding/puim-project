document.addEventListener("DOMContentLoaded", function () {

    const likesButton = document.getElementById("likesButton");
    const likesCount = document.getElementById("likesCount");
    const likesIcon = document.getElementById("likesIcon");

    const postId = document.getElementById("postId").value;
    const userIdElement = document.getElementById("userId");

    updateLikes();

    function updateLikes() {
        fetch('/posts/' + postId + '/likes')
            .then(response => {
                if (!response.ok) {
                    throw new Error('좋아요 조회 실패');
                }
                return response.json();
            })
            .then(data => {
                likesCount.innerText = data.likeCount;

                if (data.isLiked) {
                    likesIcon.classList.remove('bi-heart');
                    likesIcon.classList.add('bi-heart-fill');
                } else {
                    likesIcon.classList.remove('bi-heart-fill');
                    likesIcon.classList.add('bi-heart');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // 좋아요 버튼 클릭 바인딩
    likesButton.addEventListener("click", function () {

        // 비로그인회원
        if (userIdElement == null) {
            alert('회원 전용 기능입니다.');
        }

        const userId = userIdElement.value;

        const url = '/posts/' + postId + '/likes';

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Request failed');
                }

                updateLikes();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });
});