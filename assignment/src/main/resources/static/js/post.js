document.addEventListener("DOMContentLoaded", function () {

    document.querySelector("form").addEventListener("submit", function(event) {
        event.preventDefault();

        // 제목
        const titleText = document.querySelector("#title").value.trim();

        if (titleText.length < 1) {
            alert("제목을 입력해주세요.");
            return;
        }

        if (titleText.length > 20) {
            alert("제목은 최대 20자 까지 작성 가능합니다.");
            return;
        }

        // 내용
        const contentText = document.querySelector("#content").value.trim();

        if (contentText.length < 1) {
            alert("내용을 입력해주세요.");
            return;
        }

        if (contentText.length > 1500) {
            alert("내용은 최대 1500자 까지 작성 가능합니다.");
            return;
        }

        this.submit();
    });

});