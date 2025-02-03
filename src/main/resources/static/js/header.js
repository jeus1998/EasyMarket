document.addEventListener("DOMContentLoaded", function () {
    console.log("event?");
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
        console.log("access token o");
        // 로그인 상태: 로그아웃 & 프로필 버튼 보이기
        $("#logout-btn, #profile-link").show();
        $("#login-btn, #join-btn").hide();

        $("#logout-btn").on("click", function (event) {
            event.preventDefault();
            console.log("로그아웃 버튼 클릭됨");
            $.ajax({
                url: '/logout',
                type: 'POST',
                success: function () {
                    console.log("로그아웃 success");
                    localStorage.removeItem("accessToken");
                    window.location.href = "/";
                },
                error: function () {
                    console.log("로그아웃 fail");
                    localStorage.removeItem("accessToken");
                    window.location.href = "/";
                }
            });
        });
    } else {
        console.log("access token x");
        // 비로그인 상태: 로그인 & 회원가입 버튼 보이기
        $("#logout-btn, #profile-link").hide();
        $("#login-btn, #join-btn").show();
    }
});