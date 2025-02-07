document.addEventListener('DOMContentLoaded', function() {
      const baseUrl = '?page=';
      const paginationContainer = document.getElementById('pagination');

      const pageRowSize = 10; // 페이지네이션에 보여질 최대 페이지 수

      const maxPage = document.getElementById('totalPage').value;

      const params = new URLSearchParams(window.location.search);
      const currentPage =  params.get("page") ? params.get("page") : 1;

      const startPage = Math.trunc(currentPage / pageRowSize) * pageRowSize + 1;
      const endPage = Math.min(startPage + pageRowSize, maxPage);

        // 이전 페이지
      paginationContainer.appendChild(createPrevPageButton(baseUrl, currentPage));

      let pageCount = 0;
      for (let page = startPage; page <= endPage; page++) {

          const li = document.createElement('li');
          li.classList.add('page-item');

          const a = document.createElement('a');
          a.classList.add('page-link');
          a.href = baseUrl + page;
          a.textContent = page;

          // currentPage에 해당하는 항목에 active 클래스 추가
          if (page == currentPage) {
            li.classList.add('active');
          }

          li.appendChild(a);
          paginationContainer.appendChild(li);
          pageCount = page;
      }

        // 다음 페이지
      paginationContainer.appendChild(createNextPageButton(baseUrl, currentPage));

      function createPrevPageButton(baseUrl, currentPage) {
          const li = document.createElement('li');
          li.classList.add('page-item');

          const prevPage = Number(currentPage) - 1;
          const a = document.createElement('a');
          a.classList.add('page-link');
          if(prevPage >= 1) {
            a.href = baseUrl + prevPage;
          }
          a.textContent = '<';
          li.appendChild(a);

          return li;
      }

      function createNextPageButton(baseUrl, currentPage) {
          const li = document.createElement('li');
          li.classList.add('page-item');

          const nextPage = Number(currentPage) + 1;
          const a = document.createElement('a');
          a.classList.add('page-link');
          if(nextPage <= maxPage) {
            a.href = baseUrl + nextPage;
          }
          a.textContent = '>';
          li.appendChild(a);

          return li;
      }

    });