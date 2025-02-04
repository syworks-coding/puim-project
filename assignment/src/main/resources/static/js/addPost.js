
document.getElementById("add-post-form").addEventListener("submit", function(event) {
           event.preventDefault();  // 기본 폼 제출 동작을 방지

           const url = '/posts';  // POST 요청을 보낼 URL
           const data = {
               title: event.target.title.value,
               content: event.target.content.value,
               username: event.target.id.value
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

               window.location.href = '/';
           })
           .catch(error => {
               console.error('Error:', error);
               window.location.href = '/error-page';
           });
       });