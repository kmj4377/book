<%@ page contentType="text/html; charset=UTF-8"%>

<jsp:include page="../common/header.jsp" />

<div class="mypage-card">

	<h2 class="mypage-title">비밀번호 변경</h2>

	<form action="/usr/member/doChangePassword" method="post">

		<table class="mypage-table">
			<tr>
				<th>현재 비밀번호</th>
				<td><input type="password" name="oldPw" class="mypage-input" />
				</td>
			</tr>

			<tr>
				<th>새 비밀번호</th>
				<td><input type="password" name="newPw" class="mypage-input" />
				</td>
			</tr>

			<tr>
				<th>새 비밀번호 확인</th>
				<td><input type="password" name="newPwChk" class="mypage-input" />
				</td>
			</tr>
		</table>

		<div class="mypage-btn-row">
			<button class="mypage-btn">변경</button>
			<a href="/usr/member/mypage" class="mypage-btn outline">취소</a>
		</div>

	</form>

</div>

<jsp:include page="../common/footer.jsp" />
