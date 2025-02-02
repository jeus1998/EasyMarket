document.addEventListener("DOMContentLoaded", function () {
    console.log("event?")
    const authButtons = document.getElementById("auth-buttons");
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
        console.log("access token o")
        authButtons.innerHTML = `
                <button class="btn btn-outline-danger me-2" id="logout-btn">로그아웃</button>
                <a href="/profile" class="btn btn-outline-primary">프로필</a>
            `;
        document.getElementById("logout-btn").addEventListener("click", function () {
            localStorage.removeItem("accessToken");
            document.cookie = "refreshToken=; Max-Age=0; path=/";
            window.location.href = "/";
        });
    }
    else {
        console.log("access token x")
        authButtons.innerHTML = `
                <a href="/login" class="btn btn-outline-success me-2">로그인</a>
                <a href="/signup" class="btn btn-outline-primary">회원가입</a>
            `;
    }
});