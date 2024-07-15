const host = 'http://' + window.location.host;

$(document).ready(function () {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            // Request 헤더 설정
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        // 토큰이 없으면 기본 로그인 페이지로
        window.location.href = host + '/users/page/login';
        return;
    }

    $.ajax({
        type: 'GET',
        url: `/boards/manager`,
        contentType: 'application/json',
        success: function (response) {
            console.log(response.data);
            $('#kanban-container').append(`<div>관리보드</div>`);
            for (let i = 0; i < response.data.length; i++) {
                const board = response.data[i];
                $('#kanban-container').append(
                    `<div>
                    <button class="board-button" data-id="${board.id}">
                        ${board.boardName}
                    </button>
                </div>`
                );
            }

            $('#kanban-container').on('click', '.board-button', function() {
                const boardId = $(this).data('id');
                window.location.href = host + `/page/board?boardId=${boardId}`;
            });

        },
        error(error, status, request) {
            alert("에러");
            window.location.href = host + '/users/page/login';
        //     만료에러면 refresh 재발급 요청, 아니면 로그인 페이지 리다렉트
        }
    });

    $.ajax({
        type: 'GET',
        url: `/boards/invitation`,
        contentType: 'application/json',
        success: function (response) {
            console.log(response.data);
            $('#kanban-container').append(`<div>참여보드</div>`);
            for (let i = 0; i < response.data.length; i++) {
                const board = response.data[i];
                $('#kanban-container').append(
                    `<div>
                    <button class="board-button" data-id="${board.id}">
                        ${board.boardName}
                    </button>
                </div>`
                );
            }

            $('#kanban-container').on('click', '.board-button', function() {
                const boardId = $(this).data('id');
                window.location.href = host + `/page/board?boardId=${boardId}`;
            });

        },
        error(error, status, request) {
            alert("에러");
            window.location.href = host + '/users/page/login';
            //     만료에러면 refresh 재발급 요청, 아니면 로그인 페이지 리다렉트
        }
    });

    // Get the modal
    var modal = document.getElementById("create-board-modal");

    // Get the button that opens the modal
    var btn = document.getElementById("create-board-btn");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks the button, open the modal
    btn.onclick = function() {
        modal.style.display = "block";
    }

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    // Handle form submission
    $('#create-board-form').submit(function(e) {
        e.preventDefault();
        var boardName = $('#board-name').val();
        var BoardRequestDto = {
            boardName: boardName,
            boardInfo: $('#board-info').val()
        };
        // Add your board creation logic here
        $.ajax({
            type: 'POST',
            url: `/boards`,
            contentType: 'application/json',
            data: JSON.stringify(BoardRequestDto),
            success: function (response) {
                console.log(response.data);
                alert("보드 생성 성공: boardName");
                console.log("Board created with name: " + boardName);
                modal.style.display = "none";
                location.reload(true);
            },
            error(error, status, request) {
                alert("보드 생성 실패: boardName");
                console.log("Board created with name: " + boardName);
                modal.style.display = "none";
                location.reload(true);
            }
        });
    });
})
// End.$(document).ready

// 토큰 가져오기
function getToken() {

    let auth = Cookies.get('Authorization');

    if(auth === undefined) {
        return '';
    }

    // kakao 로그인 사용한 경우 Bearer 추가
    if(auth.indexOf('Bearer') === -1 && auth !== ''){
        auth = 'Bearer ' + auth;
    }

    return auth;
}
