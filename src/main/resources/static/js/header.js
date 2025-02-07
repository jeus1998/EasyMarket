document.addEventListener("DOMContentLoaded", function () {
    console.log("event?");
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
        console.log("access token o");
        checkUserStatus(accessToken);
    }
    else {
        console.log("access token x");
        // 비로그인 상태: 로그인 & 회원가입 버튼 보이기
        $("#logout-btn, #profile-link").hide();
        $("#login-btn, #join-btn").show();
    }

    // 로그아웃 버튼 이벤트 핸들러
    $("#logout-btn").on("click", function (event) {
        event.preventDefault();
        console.log("로그아웃 버튼 클릭됨");
        $.ajax({
            url: '/logout',
            type: 'POST',
            success: function () {
                console.log("로그아웃 success");
                localStorage.removeItem("accessToken");
                sessionStorage.removeItem('currentUser');
                window.location.href = "/";
            },
            error: function () {
                console.log("로그아웃 fail");
                localStorage.removeItem("accessToken");
                sessionStorage.removeItem('currentUser');
                window.location.href = "/";
            }
        });
    });
});

function checkUserStatus(accessToken) {
    $.ajax({
        url: '/check', // 사용자 정보를 가져오는 API 경로
        type: 'GET',
        headers: {
            'access': accessToken
        },
        success: function (user) {
            // 정상 응답: access token 만료 X, 사용자 정보 반환
            console.log("사용자 정보:", user);
            sessionStorage.setItem('currentUser', JSON.stringify(user));

            // 로그인 상태: 로그아웃 & 프로필 버튼 보이기
            $("#logout-btn, #profile-link").show();
            $("#login-btn, #join-btn").hide();
        },
        error: function (xhr) {
            if (xhr.status === 401) {
                // access token 만료 O, 토큰 갱신 필요
                refreshAccessToken();
            }
            else {
                // 서명 오류 또는 기타 오류
                console.error("Error fetching user data:", xhr);
                localStorage.removeItem("accessToken");
                window.location.href = "/login"; // 로그인 페이지로 리다이렉트
            }
        }
    });
}

function refreshAccessToken() {
    $.ajax({
        url: '/refresh', // 토큰 갱신 API 경로
        type: 'POST',
        success: function (data, textStatus, xhr) {
            let accessToken = xhr.getResponseHeader('access');
            if (accessToken) {
                localStorage.setItem("accessToken", accessToken);
                // 갱신된 토큰으로 다시 사용자 상태 확인
                checkUserStatus(accessToken);
            }
        },
        error: function () {
            // 토큰 갱신 실패
            localStorage.removeItem("accessToken");
            window.location.href = "/login"; // 로그인 페이지로 리다이렉트
        }
    });
}
