const host = 'http://' + window.location.host;
let targetId;
let folderTargetId;

$(document).ready(function () {
    const auth = getToken();
    const boardId = new URLSearchParams(window.location.search).get('boardId');

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

    $('#kanban-container').append(`<div>${boardId}</div>`);


    $('#kanban-container').append(exHtml());
    var columnsRequestDto = {
        boardId: boardId
    };
    $.ajax({
        type: 'GET',
        url: `/columns`,
        contentType: 'application/json',
        data: JSON.stringify(columnsRequestDto),
        success: function (response) {
            console.log(response.data);
            // private String columnName;
            // private Long columnOrder;
            // private LocalDateTime createdAt;
            // private LocalDateTime modifiedAt;
            // private Long columnId;
            for (let i = 0; i < response.data.length; i++) {
                const column = response.data[i];
                $('#kanban-container').append(
                    `<div class="kanban-column" id="todo-column" draggable="true" ondragstart="drag(event)" ondrop="dropOnColumn(event)" ondragover="allowDrop(event)">
                        <h2>${column.columnName}</h2>
                        <div class="kanban-cards" id="todo-cards">
                            <div class="kanban-card" id="task1" draggable="true" ondragstart="drag(event)">Task 1</div>
                            <div class="kanban-card" id="task2" draggable="true" ondragstart="drag(event)">Task 2</div>
                        </div>
                        <button class="add-card-btn" onclick="addCard('todo-column')">Add Card</button>
                    </div>`
                );
            }

            // $('#kanban-container').on('click', '.board-button', function () {
            //     const boardId = $(this).data('id');
            //     window.location.href = host + `/board-page`;
            // });

        },
        error(error, status, request) {
            alert("columns 호출 에러");
            // window.location.href = host + '/users/page/login';
            //     만료에러면 refresh 재발급 요청, 아니면 로그인 페이지 리다렉트
        }
    });
})

function exHtml() {
    return ` <div class="kanban-column" id="todo-column" draggable="true" ondragstart="drag(event)" ondrop="dropOnColumn(event)" ondragover="allowDrop(event)">
        <h2>To Do</h2>
        <div class="kanban-cards" id="todo-cards">
            <div class="kanban-card" id="task1" draggable="true" ondragstart="drag(event)">Task 1</div>
            <div class="kanban-card" id="task2" draggable="true" ondragstart="drag(event)">Task 2</div>
        </div>
        <button class="add-card-btn" onclick="addCard('todo-column')">Add Card</button>
    </div>
    <div class="kanban-column" id="in-progress-column" draggable="true" ondragstart="drag(event)" ondrop="dropOnColumn(event)" ondragover="allowDrop(event)">
        <h2>In Progress</h2>
        <div class="kanban-cards" id="in-progress-cards"></div>
        <button class="add-card-btn" onclick="addCard('in-progress-column')">Add Card</button>
    </div>
    <div class="kanban-column" id="done-column" draggable="true" ondragstart="drag(event)" ondrop="dropOnColumn(event)" ondragover="allowDrop(event)">
        <h2>Done</h2>
        <div class="kanban-cards" id="done-cards"></div>
        <button class="add-card-btn" onclick="addCard('done-column')">Add Card</button>
    </div>`;
}

// function initializeSortable() {
//     var columns = document.querySelectorAll('.column');
//     columns.forEach(column => {
//         new Sortable(column, {
//             group: 'shared',
//             animation: 150
//         });
//     });
//
//     new Sortable(document.getElementById('board'), {
//         animation: 150,
//         draggable: '.column'
//     });
// }

// 토큰 가져오기
function getToken() {

    let auth = Cookies.get('Authorization');
    if (auth === undefined) {
        return '';
    }

    // kakao 로그인 사용한 경우 Bearer 추가
    if (auth.indexOf('Bearer') === -1 && auth !== '') {
        auth = 'Bearer ' + auth;
    }

    return auth;
}

// Drag and Drop Functions
function allowDrop(event) {
    event.preventDefault();
}

function drag(event) {
    event.dataTransfer.setData("text", event.target.id);
    event.dataTransfer.setData("type", event.target.className);
}

function dropOnColumn(event) {
    event.preventDefault();
    const data = event.dataTransfer.getData("text");
    const element = document.getElementById(data);
    const type = event.dataTransfer.getData("type");
    const parentType = event.target.parentElement.className;
    // alert(`parentType : ${parentType}`);

    // Prevent kanban-column from being dropped inside another kanban-column
    if (type.includes("column")) {
        // alert(`drop : ${element.textContent}이(가) ${event.target.parentElement.querySelector('h2').textContent}로 갈 뻔 했습니다....휴...`);


        const kanbanBoard = document.getElementById("kanban-board");
        const columns = Array.from(kanbanBoard.children);
        const targetParent = document.getElementById(event.target.parentElement.id);
        const targetIndex = columns.indexOf(targetParent);

        alert(`column : ${columns.indexOf(element)} , targetIndex : ${columns.indexOf(targetParent)}`);
        // Adjust the insertion position
        const adjustedIndex = columns.indexOf(element) < targetIndex ? targetIndex : targetIndex + 1;

        kanbanBoard.insertBefore(element, kanbanBoard.children[adjustedIndex]);

        return;
    }

    if (event.target.classList.contains("kanban-cards")) {
        event.target.appendChild(element);
        alert(`drop : ${element.textContent}이(가) ${event.target.parentElement.querySelector('h2').textContent}로 이동되었습니다.`);
    } else if (event.target.classList.contains("kanban-card")) {
        event.target.parentElement.insertBefore(element, event.target.nextSibling);
        alert(`drop : ${element.textContent}이(가) ${event.target.parentElement.parentElement.querySelector('h2').textContent}로 이동되었습니다.`);
    }
}

function dropOnBoard(event) {
    // alert("aaa");
}

function updateColumnOrder() {
    const columns = document.querySelectorAll(".kanban-column");
    columns.forEach((column, index) => {
        column.setAttribute("data-order", index + 1);
        alert(`${column.querySelector('h2').textContent}의 순서가 ${index + 1}번으로 변경되었습니다.`);
    });
}


function moveTodoToFirst() {
    const kanbanBoard = document.getElementById("kanban-board");
    const todoColumn = document.getElementById("todo-column");
    kanbanBoard.insertBefore(todoColumn, kanbanBoard.firstChild);
    updateColumnOrder();
}

function addCard(columnId) {
    const cardText = prompt('Enter card text:');
    if (cardText) {
        const column = document.getElementById(columnId);
        const newCard = document.createElement('div');
        newCard.className = 'kanban-card';
        newCard.textContent = cardText;
        newCard.setAttribute('draggable', 'true');
        newCard.setAttribute('ondragstart', 'drag(event)');
        newCard.id = 'card-' + Date.now();
        column.querySelector('.kanban-cards').appendChild(newCard);
    }
}

// Login/Logout Functions
function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    if (email === 'user' && password === 'pass') {
        localStorage.setItem('loggedIn', 'true');
        document.getElementById('login-form').style.display = 'none';
        document.getElementById('logout-btn').style.display = 'block';
    } else {
        alert('Invalid credentials');
    }
}

function logout() {
    localStorage.removeItem('loggedIn');
    document.getElementById('login-form').style.display = 'flex';
    document.getElementById('logout-btn').style.display = 'none';
}

// Check Login Status on Page Load
// window.onload = function() {
//     if (localStorage.getItem('loggedIn') === 'true') {
//         document.getElementById('login-form').style.display = 'none';
//         document.getElementById('logout-btn').style.display = 'block';
//     } else {
//         document.getElementById('login-form').style.display = 'flex';
//         document.getElementById('logout-btn').style.display = 'none';
//     }
// }