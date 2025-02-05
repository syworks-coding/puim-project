
document.getElementById("editPostForm").addEventListener("submit", function(event) {
           event.preventDefault();  // 기본 폼 제출 동작을 방지

           const postId = event.target.postId.value;

           const url = `/posts/${postId}`;
           const data = {
               title: event.target.title.value,
               content: event.target.content.value
           };

           fetch(url, {
               method: 'PATCH',
               headers: {
                   'Content-Type': 'application/json'
               },
               body: JSON.stringify(data)
           })
           .then(response => {

               if (!response.ok) {
                   throw new Error('Request failed');
               }

               window.location.href = url;
           })
           .catch(error => {
               console.error('Error:', error);
               window.location.href = '/error-page';
           });
       });