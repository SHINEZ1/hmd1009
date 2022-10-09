'use strict';
//textArea => ck_editor 대체
ClassicEditor
		.create( document.querySelector( '#pcontent' ), {
		 plugin:['ListStyle','Markdown','MediaEmbed','MediaEmbedToolbar'],
			toolbar: {
				items: [
					'heading',
					'|',
					'underline',
					'bold',
					'italic',
					'link',
					'bulletedList',
					'numberedList',
					'todoList',
					'|',
					'outdent',
					'indent',
					'|',
					'imageInsert',
					'imageUpload',
					'blockQuote',
					'insertTable',
					'mediaEmbed',
					'markdown',
					'undo',
					'redo',
					'|',
					'highlight',
					'fontFamily',
					'fontColor',
					'fontBackgroundColor',
					'fontSize',
					'|',
					'htmlEmbed',
					'specialCharacters'
				]
			},
			language: 'en',
			image: {
				toolbar: [
					'imageTextAlternative',
					'imageStyle:full',
					'imageStyle:side'
				]
			},
			table: {
				contentToolbar: [
					'tableColumn',
					'tableRow',
					'mergeTableCells',
					'tableCellProperties',
					'tableProperties'
				]
			},
		})
		.then( editor => {

			window.editor = editor;
		} )
		.catch( error => {
			console.error( error );
		} );

const $post = document.querySelector('.post-wrap');
const category = ($post?.dataset.code)? $post.dataset.code : '';

//등록
const $writeBtn = document.getElementById('writeBtn');
$writeBtn?.addEventListener("click", e=>{
  writeForm.submit();
});
//목록
const $listBtn = document.getElementById('listBtn');
$listBtn?.addEventListener("click",e=>{
  const url = `/post/list?category=${category}`;
  location.href = url;
});
//분류자동 선택
const $options = document.querySelectorAll('#pcategory option');
[...$options].find(option=>option.value===category).setAttribute('selected','selected');