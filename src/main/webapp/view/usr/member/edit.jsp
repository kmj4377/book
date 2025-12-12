<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../common/header.jsp" />

<div class="mypage-card">

    <h2 class="mypage-title">정보 수정</h2>

    <form action="/usr/member/doEdit" method="post">

        <table class="mypage-table">

            <!-- 아이디 (수정 불가) -->
            <tr>
                <th>아이디</th>
                <td>
                    <div class="mypage-readonly">
                        ${member.loginId}
                    </div>
                    <div class="mypage-hint">
                        아이디는 변경할 수 없습니다.
                    </div>
                </td>
            </tr>

            <!-- 이름 -->
            <tr>
                <th>이름</th>
                <td>
                    <input type="text"
                           name="name"
                           value="${member.name}"
                           class="mypage-input"
                           required />
                </td>
            </tr>

            <!-- 닉네임 -->
            <tr>
                <th>닉네임</th>
                <td>
                    <input type="text"
                           name="nickname"
                           value="${member.nickname}"
                           class="mypage-input"
                           required />
                </td>
            </tr>

            <!-- 이메일 -->
            <tr>
                <th>이메일</th>
                <td>
                    <input type="email"
                           name="email"
                           value="${member.email}"
                           class="mypage-input" />
                </td>
            </tr>

            <!-- 가입일 (읽기 전용) -->
            <tr>
                <th>가입일</th>
                <td>
                    <div class="mypage-readonly">
                        ${fn:replace(member.regDate, 'T', ' ')}
                    </div>
                </td>
            </tr>

        </table>

        <div class="mypage-btn-row">
            <button type="submit" class="mypage-btn">저장</button>
            <a href="/usr/member/mypage" class="mypage-btn outline">취소</a>
            <a href="/usr/member/password" class="mypage-btn outline">비밀번호 변경</a>
        </div>

    </form>

</div>

<jsp:include page="../common/footer.jsp" />
