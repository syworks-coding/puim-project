document.addEventListener('DOMContentLoaded', function() {
      const startPage = 1; // 시작 페이지 번호
      const endPage = 10;  // 끝 페이지 번호
      const currentPage = 1; // 현재 페이지 번호

      const baseUrl = '?page=';

      const paginationContainer = document.getElementById('pagination');
      let pageCount = 0;

      // startPage부터 endPage까지 페이지 버튼 생성
      for (let page = startPage; page <= endPage; page++) {
        const li = document.createElement('li');
        li.classList.add('page-item');

        const a = document.createElement('a');
        a.classList.add('page-link');
        a.href = baseUrl + page;
        a.textContent = page;

        // currentPage에 해당하는 항목에 active 클래스 추가
        if (page === currentPage) {
          li.classList.add('active');
        }

        li.appendChild(a);
        paginationContainer.appendChild(li);
        pageCount = page;
      }

      const li = document.createElement('li');
      li.classList.add('page-item');

      const nextPage = pageCount + 1;
      const a = document.createElement('a');
      a.classList.add('page-link');
      a.href = baseUrl + nextPage;
      a.textContent = '>';
      li.appendChild(a);
      paginationContainer.appendChild(li);

    });