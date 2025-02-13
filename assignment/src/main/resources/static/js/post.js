document.addEventListener("DOMContentLoaded", function () {

    const titleElement = document.querySelector("#title");
    const contentElement = document.querySelector("#content");

    const contentLengthElement = document.querySelector("#contentLength");

    const maxTitleLength = 20;
    const maxContentLength = 80;

    function renderContentLength(contentElement, maxContentLength) {
        const length = contentElement.value.length;
        if(length >= maxContentLength) {
            const origin = contentElement.value;
            contentElement.value = origin.slice(contentElement.value, maxContentLength);
        }

        contentLengthElement.textContent = Math.min(length, maxContentLength) + '/' + maxContentLength + '자';
    }

    renderContentLength(contentElement, maxContentLength);

    contentElement.addEventListener("input", () => renderContentLength(contentElement, maxContentLength));

    document.querySelector("form").addEventListener("submit", function(event) {
        event.preventDefault();

        // 제목
        const titleText = titleElement.value.trim();

        if (titleText.length < 1) {
            alert("제목을 입력해주세요.");
            return;
        }

        if (titleText.length > maxTitleLength) {
            alert("제목은 최대 " + maxTitleLength + "자 까지 작성 가능합니다.");
            return;
        }

        // 내용
        const contentText = contentElement.value.trim();

        if (contentText.length < 1) {
            alert("내용을 입력해주세요.");
            return;
        }

        if (contentText.length > maxContentLength) {
            alert("내용은 최대 " + maxContentLength + "자 까지 작성 가능합니다.");
            return;
        }

        this.submit();
    });

});