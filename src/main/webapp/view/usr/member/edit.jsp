<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="../common/header.jsp" />

<style>
.mypage-input.error {
	border: 2px solid #ef4444;
}

.mypage-input.error::placeholder {
	color: #ef4444;
	font-weight: 600;
}
</style>

<div class="mypage-card">

	<h2 class="mypage-title">정보 수정</h2>

	<form action="/usr/member/doEdit" method="post" novalidate
		onsubmit="return validateEditForm();">

		<table class="mypage-table">

			<tr>
				<th>아이디</th>
				<td>
					<div class="mypage-readonly">${member.loginId}</div>
					<div class="mypage-hint">아이디는 변경할 수 없습니다.</div>
				</td>
			</tr>

			<tr>
				<th>이름</th>
				<td><input type="text" name="name" id="name"
					value="${member.name}" class="mypage-input"
					placeholder="이름을 입력해주세요" /></td>
			</tr>

			<tr>
				<th>닉네임</th>
				<td><input type="text" name="nickname" id="nickname"
					value="${member.nickname}" class="mypage-input"
					placeholder="닉네임을 입력해주세요" /></td>
			</tr>

			<tr>
				<th>이메일</th>
				<td><input type="email" name="email" value="${member.email}"
					class="mypage-input" /></td>
			</tr>

			<tr>
				<th>가입일</th>
				<td>
					<div class="mypage-readonly">${fn:replace(member.regDate, 'T', ' ')}
					</div>
				</td>
			</tr>

		</table>

		<div class="mypage-btn-row">
			<button type="submit" class="mypage-btn">저장</button>
			<a href="/usr/member/mypage" class="mypage-btn outline">취소</a>
		</div>

	</form>
</div>

<script>
function validateEditForm() {
    let valid = true;

    const name = document.getElementById("name");
    const nickname = document.getElementById("nickname");

    [name, nickname].forEach(input => {
        input.classList.remove("error");
    });

    if (!name.value.trim()) {
        name.value = "";
        name.placeholder = "이름을 입력해주세요";
        name.classList.add("error");
        valid = false;
    }

    if (!nickname.value.trim()) {
        nickname.value = "";
        nickname.placeholder = "닉네임을 입력해주세요";
        nickname.classList.add("error");
        valid = false;
    }

    return valid;
}

document.querySelectorAll(".mypage-input").forEach(input => {
    input.addEventListener("input", () => {
        input.classList.remove("error");
    });
});
</script>

<jsp:include page="../common/footer.jsp" />
