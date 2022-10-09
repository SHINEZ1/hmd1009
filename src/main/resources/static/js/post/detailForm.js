'use strict';
//textArea => ck_editor 대체
//ClassicEditor
//		.create( document.querySelector( '#pcontent' ), {
//		 plugin:['ListStyle','Markdown','MediaEmbed','MediaEmbedToolbar'],
//			toolbar: {
//				items: [
//					'heading',
//					'|',
//					'underline',
//					'bold',
//					'italic',
//					'link',
//					'bulletedList',
//					'numberedList',
//					'todoList',
//					'|',
//					'outdent',
//					'indent',
//					'|',
//					'imageInsert',
//					'imageUpload',
//					'blockQuote',
//					'insertTable',
//					'mediaEmbed',
//					'markdown',
//					'undo',
//					'redo',
//					'|',
//					'highlight',
//					'fontFamily',
//					'fontColor',
//					'fontBackgroundColor',
//					'fontSize',
//					'|',
//					'htmlEmbed',
//					'specialCharacters'
//				]
//			},
//			language: 'en',
//			image: {
//				toolbar: [
//					'imageTextAlternative',
//					'imageStyle:full',
//					'imageStyle:side'
//				]
//			},
//			table: {
//				contentToolbar: [
//					'tableColumn',
//					'tableRow',
//					'mergeTableCells',
//					'tableCellProperties',
//					'tableProperties'
//				]
//			},
//		})
//		.then( editor => {
//			window.editor = editor;
//			editor.isReadOnly = true;  //읽기모드적용
//		} )
//		.catch( error => {
//			console.error( error );
//		} );

const $post = document.querySelector('.post-wrap');
const category = ($post?.dataset.code)? $post.dataset.code : '';

//답글
const $replyBtn = document.getElementById('replyBtn');
$replyBtn?.addEventListener('click',e=>{
  const url = `/post/${postId.value}/reply?category=${category}`;
  location.href = url;
});
//수정
const $editBtn = document.getElementById('editBtn');
$editBtn?.addEventListener('click',e=>{
  const url = `/post/${postId.value}/edit?category=${category}`;
  location.href = url;
});
//삭제
const $delBtn = document.getElementById('delBtn');
$delBtn?.addEventListener('click',e=>{
  if(confirm('삭제하시겠습니까?')){
    const url = `/post/${postId.value}/del?category=${category}`;
    location.href = url;
  }
});
//목록
const $listBtn = document.getElementById('listBtn');
$listBtn?.addEventListener('click',e=>{
  const url = `/post/list?category=${category}`;
  console.log('url='+url);
  location.href=url;
});

//전역변수
const reply = {
  postId:document.getElementById('postId'),
  pcategory:document.getElementById('pcategory'),
  recontent:document.getElementById('rcontent'),
  //mode: null
}

//입력데이터 가져오기
function getReplyData(){

  const postId = reply.postId.value;
  const pcategory = reply.pcategory.value;
  const rcontent = reply.rcontent.text;
  //변수이름을 property key, 변수값을 property value
  return {postId, pcategory, rcontent};
}

addReplyBtn.addEventListener('click', e => {
  addReply(getReplyData());
  // console.log(reply.postId, reply.pcategory, reply.nickname, reply.email, reply.rcontent);
});

//등록
function addReply(reply){
const url = 'http://localhost:9080/api/reply';
fetch( url,{            //url
  method:'POST',        //http method
  headers:{             //http header
    'Content-Type':'application/json',
    'Accept':'application/json'
  },
  body: JSON.stringify(reply)   //http body      // js객체 => json포맷의 문자열
}).then(res=>res.json())
  .then(data=>{
    console.log(data)
    // findAll();
    })
  .catch(err=>console.log(err));
}
editReplyBtn.addEventListener('click',e=>{
  //1)유효성 체크
  // validChk()
  //2)수정처리
  update(reply.productId.value, getInputData());
});

//삭제 클릭
delReplyBtn.addEventListener('click',e=>{
  if(!confirm('삭제하시겠습니까?')) return;
  const id = form.productId.value; //상품아이디
  deleteById(id);
  clearForm();
});
