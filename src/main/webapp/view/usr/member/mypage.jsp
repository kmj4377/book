<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../common/header.jsp" />

<div class="mypage-card">

    <h2 class="mypage-title">마이페이지</h2>

    <table class="mypage-table">
        <tbody>

        <tr>
            <th>아이디</th>
            <td>${member.loginId}</td>
        </tr>

        <tr>
            <th>이름</th>
            <td>${member.name}</td>
        </tr>

        <tr>
            <th>닉네임</th>
            <td>${member.nickname}</td>
        </tr>

        <tr>
            <th>이메일</th>
            <td>${member.email}</td>
        </tr>

       <tr>
    <th>이메일 인증</th>
    <td>
        <div class="email-auth-wrap">
            <c:choose>
    <c:when test="${member.emailVerified == 1}">
        <span class="text-green-600 font-bold">인증</span>
    </c:when>
    <c:otherwise>
        <span class="text-red-600 font-bold">미인증</span>
        <a href="/usr/member/emailAuth" class="btn btn-sm btn-neutral ml-2">
            인증하기
        </a>
    </c:otherwise>
</c:choose>

        </div>
    </td>
</tr>






        <tr>
            <th>회원 유형</th>
            <td>
                <c:choose>
                    <c:when test="${sessionScope.loginedMember.memberAuthType == 'LOCAL'}">
                        일반 회원
                    </c:when>
                    <c:when test="${sessionScope.loginedMember.memberAuthType == 'KAKAO'}">
                        카카오 회원
                    </c:when>
                    <c:when test="${sessionScope.loginedMember.memberAuthType == 'NAVER'}">
                        네이버 회원
                    </c:when>
                    <c:otherwise>
                        구글 회원
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>

        <!-- 가입일 -->
        <tr>
            <th>가입일</th>
            <td>
                ${fn:replace(member.regDate, 'T', ' ')}
            </td>
        </tr>

        <!-- 최근 로그인 (있을 때만 표시) -->
        <c:if test="${not empty member.lastLoginAt}">
            <tr>
                <th>최근 로그인</th>
                <td>
                    ${fn:replace(member.lastLoginAt, 'T', ' ')}
                </td>
            </tr>
        </c:if>

        </tbody>
    </table>

    <div class="mypage-btn-row">
        <a href="/usr/member/edit" class="mypage-btn">정보 수정</a>

        <c:if test="${sessionScope.loginedMember.memberAuthType == 'LOCAL'}">
            <a href="/usr/member/password" class="mypage-btn">비밀번호 변경</a>
        </c:if>
    </div>

</div>

<jsp:include page="../common/footer.jsp" />
