<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="로그인"/>

<%@ include file="../common/header.jsp" %>

<link rel="stylesheet" href="/css/login.css">

<!-- 공통 알림 메시지 -->
<c:if test="${param.msg == 'login-required'}">
    <script>alert('로그인 후 이용해주세요.');</script>
</c:if>


<script>
    const loginFormSubmit = function(form) {
        form.loginId.value = form.loginId.value.trim();
        form.loginPw.value = form.loginPw.value.trim();

        if (form.loginId.value.length === 0) {
            alert('아이디는 필수 입력 정보입니다.');
            form.loginId.focus();
            return false;
        }

        if (form.loginPw.value.length === 0) {
            alert('비밀번호는 필수 입력 정보입니다.');
            form.loginPw.focus();
            return false;
        }

        return true;
    }
</script>

<section class="login-container">
    <div class="login-box">

        <!-- 일반 로그인 -->
        <form action="/usr/member/doLogin" method="post" onsubmit="return loginFormSubmit(this);">
            <fieldset class="fieldset">
                <legend class="fieldset-legend">로그인</legend>

                <label class="label">아이디</label>
                <input name="loginId" type="text" class="input" placeholder="아이디 입력"/>

                <label class="label">비밀번호</label>
                <input name="loginPw" type="password" class="input" placeholder="비밀번호 입력"/>

                <button class="btn btn-neutral w-full mt-3">로그인</button>
            </fieldset>
        </form>

        <!-- 카카오 로그인 -->
        <div class="kakao-btn-wrapper mt-3">
            <a id="kakaoLoginBtn" href="#">
                <img src="/img/kakao_login.png" alt="카카오 로그인">
            </a>
        </div>

        <script>
        document.getElementById('kakaoLoginBtn').addEventListener('click', function(e) {
            e.preventDefault();

            const clientId = "${kakaoRestKey}";
            const redirectUri = "${kakaoRedirectUri}";

            const kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + encodeURIComponent(clientId)
                + "&redirect_uri=" + encodeURIComponent(redirectUri)
                + "&response_type=code"
                + "&prompt=login";

            window.location.href = kakaoAuthUrl;
        });
        </script>

        <!-- 네이버 로그인 -->
        <a href="${naverLoginUrl}" class="naver-btn">
            <img src="/img/naver-icon.png" class="naver-icon">
            <span class="naver-text">네이버 로그인</span>
        </a>

        <!-- 구글 로그인 -->
        <a href="${googleLoginUrl}" class="google-login-btn">
            <img src="/img/google-icon.png" class="google-icon">
            <span>Google 계정으로 계속하기</span>
        </a>

        <!-- 회원가입 -->
        <div class="mt-4 text-center">
            <span>계정이 없으신가요? </span>
            <a href="/usr/member/join" class="btn btn-sm btn-outline">회원가입</a>
        </div>

        <!-- 뒤로가기 -->
        <div class="mt-2 text-center">
            <button class="btn btn-xs btn-outline" onclick="history.back();">뒤로가기</button>
        </div>

    </div>
</section>

<%@ include file="../common/footer.jsp" %>
