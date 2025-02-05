document.addEventListener("DOMContentLoaded", function () {

    // 게시물 수정 버튼
    const editButton = document.getElementById("postEditButton");

    const postId = document.getElementById("postId").value;
    const userId = null;
    const parentId = null;

    editButton.addEventListener("click", function () {

        window.location.href = window.location.pathname + "/edit";
    });
});