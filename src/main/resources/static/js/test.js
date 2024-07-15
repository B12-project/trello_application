$(document).ready(function () {
    const auth = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3NDcyY2M0Ny1iMzg5LTQzOGItODRhMS0xYWIxZmI5OTVmNjUiLCJzdWIiOiJiMTR1c2VyQGdtYWlsLmNvbSIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNzIwNzk3NjcxLCJleHAiOjE3MjA4MDQ4NzEsInRva2VuVHlwZSI6ImFjY2VzcyJ9.pIu5NaSFWwfBnY1p3wHTU-H_T3hNUMAWKiJZfD6ebsA";

    // 칼럼 데이터를 메모리에 저장할 변수
    var columnMap = {};

    // 카드 데이터를 서버에서 불러옵니다
    function loadCards() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/cards',
            headers: {
                'Authorization': 'Bearer ' + auth
            },
            success: function (response) {
                if (Array.isArray(response)) {
                    response.forEach(card => {
                        console.log("카드 status: " + card.status);
                        addCardToColumn(card);
                    });
                } else {
                    console.error("Expected an array but got:", response);
                }
            },
            error: function (error) {
                console.error("Error loading cards:", error);
                alert("카드 데이터를 불러오는 중 오류가 발생했습니다.");
            },
        });
    }

    // 칼럼 데이터를 서버에서 불러옵니다
    function loadColumns() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/columns',
            headers: {
                'Authorization': 'Bearer ' + auth
            },
            success: function (response) {
                console.log("Column data:", response);
                if (Array.isArray(response)) {
                    response.forEach(column => {
                        console.log("컬럼: " + column.columnsStatus);
                        columnMap[column.columnsStatus] = column.id; // 상태를 ID에 매핑
                        addColumnToBoard(column);
                    });
                    initializeSortable(); // 드래그 가능 초기화
                } else {
                    console.error("Expected an array but got:", response);
                }
            },
            error: function (error) {
                console.error("Error loading columns:", error);
                alert("칼럼 데이터를 불러오는 중 오류가 발생했습니다.");
            },
        });
    }

    // 카드 추가
    function addCardToColumn(card) {
        var columnId = columnMap[card.status]; // 카드 상태에 맞는 칼럼 ID 찾기
        if (columnId) {
            var column = document.getElementById(`column${columnId}`);
            if (column) {
                var newCard = document.createElement('div');
                newCard.className = 'card';
                newCard.textContent = card.title;
                column.appendChild(newCard); // 칼럼의 마지막에 카드 추가
            } else {
                console.error("Column with ID " + columnId + " not found.");
            }
        } else {
            console.error("No column found for status: " + card.status);
        }
    }

    // 칼럼을 보드에 추가
    function addColumnToBoard(columnData) {
        var board = document.getElementById('board');
        var newColumn = document.createElement('div');
        newColumn.className = 'column';
        newColumn.id = `column${columnData.id}`;
        newColumn.innerHTML = `<div class="column-header">${columnData.columnsStatus}</div>
            <button class="add-button" onclick="showModal()">할 일 추가하기</button>`;
        board.appendChild(newColumn);

        // 칼럼에 포함된 카드들을 추가
        if (Array.isArray(columnData.cardList)) {
            columnData.cardList.forEach(card => {
                var newCard = document.createElement('div');
                newCard.className = 'card';
                newCard.textContent = card.title;
                newColumn.appendChild(newCard); // 칼럼의 마지막에 카드 추가
            });
        } else {
            console.error("Expected an array of cards but got:", columnData.cardList);
        }
    }

    // 칼럼 및 보드를 드래그 가능하게 초기화
    function initializeSortable() {
        var columns = document.querySelectorAll('.column');
        columns.forEach(column => {
            new Sortable(column, {
                group: 'shared',
                animation: 150
            });
        });

        new Sortable(document.getElementById('board'), {
            animation: 150,
            draggable: '.column'
        });
    }

    // 모달 열기
    function showModal() {
        document.getElementById('modal').style.display = 'flex';
    }

    // 모달 닫기
    function closeModal() {
        document.getElementById('modal').style.display = 'none';
    }

    // 할 일 추가
    function addTask() {
        var title = document.getElementById('taskTitle').value;
        var status = document.getElementById('taskStatus').value;
        var content = document.getElementById('taskContent').value;
        var dueDate = document.getElementById('taskDueDate').value;
        var assignee = document.getElementById('taskAssignee').value;

        if (!title) {
            alert("제목은 필수 항목입니다.");
            return;
        }

        // task 객체 생성
        var task = {
            title: title,
            content: content,
            deadDate: dueDate ? new Date(dueDate).toISOString() : null,
            assigneeId: assignee ? parseInt(assignee) : null,
            status: status
        };

        console.log("Created task:", task);

        // cardDto 객체 생성
        var cardDto = {
            title: title,
            content: content,
            deadDate: dueDate ? new Date(dueDate).toISOString() : null,
            assigneeId: assignee ? parseInt(assignee) : null,
            status: status,
            tasks: [task]
        };

        console.log("Created cardDto with task:", cardDto);

        addCard(cardDto);
        closeModal();
    }

    // 카드 추가 기능 (백엔드와 통신)
    function addCard(cardDto) {
        console.log("Sending cardDto:", cardDto);

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/cards',
            crossOrigin: true,
            contentType: 'application/json',
            data: JSON.stringify(cardDto),
            success: function (response) {
                alert("생성 완료");
                addCardToColumn(response); // 응답 받은 카드로 칼럼에 추가
            },
            error: function (error) {
                console.error("Error:", error);
                alert("다시 입력해 주세요");
            },
        });
    }

    // 페이지 로드 후 카드 및 칼럼 데이터 로드

    loadCards();
    loadColumns();
});
