<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="회원가입"/>

<%@ include file="/view/usr/common/header.jsp" %>

<script>
	let validLoginId = null;

	// --------------------- 회원가입 submit ---------------------
	const joinFormSubmit = function(form) {

		$('#pwMsg').text('');
		$('#pwChkMsg').text('');
        $('#nameMsg').text('');
        $('#nicknameMsg').text('');

		form.loginId.value = form.loginId.value.trim();
		form.loginPw.value = form.loginPw.value.trim();
		form.loginPwChk.value = form.loginPwChk.value.trim();
		form.name.value = form.name.value.trim();
		form.nickname.value = form.nickname.value.trim();

		// (1) 아이디 필수
        if (form.loginId.value.length === 0) {
            $('#chkMsg').removeClass('text-green-500').addClass('text-red-500');
            $('#chkMsg').text('아이디는 필수 입력 정보입니다.');
            form.loginId.focus();
            return false;
        }

        // (2) 아이디 중복 검사 여부
        if (form.loginId.value !== validLoginId) {
            loginIdDupChk(form.loginId);
            return false;
        }

		// (3) 비밀번호 필수
		if (form.loginPw.value.length === 0) {
			$('#pwMsg').text('비밀번호는 필수 입력 정보입니다.');
			form.loginPw.focus();
			return false;
		}

		// (4) 비밀번호 확인
		if (form.loginPw.value !== form.loginPwChk.value) {
			$('#pwChkMsg').text('비밀번호가 일치하지 않습니다.');
			form.loginPw.value = '';
			form.loginPwChk.value = '';
			form.loginPw.focus();
			return false;
		}

		// (5) 이름 필수
		if (form.name.value.length === 0) {
			$('#nameMsg').text('이름은 필수 입력 정보입니다.');
			form.name.focus();
			return false;
		}

		// (6) 닉네임 필수
		if (form.nickname.value.length === 0) {
			$('#nicknameMsg').text('닉네임은 필수 입력 정보입니다.');
			form.nickname.focus();
			return false;
		}

		return true;
	}

	// --------------------- 아이디 중복 검사 ---------------------
	const loginIdDupChk = function(el) {
		let chkMsg = $('#chkMsg');
		el.value = el.value.trim();

		if (el.value.length === 0) {
			chkMsg.removeClass('text-green-500').addClass('text-red-500');
			chkMsg.text('아이디는 필수 입력 정보입니다.');
			validLoginId = null;
			return;
		}

		$.ajax({
			url: '/usr/member/loginIdDupChk',
			type: 'get',
			data: { loginId: el.value },
			dataType: 'json',
			success: function(data) {

                if (!data || !data.rsCode) {
                    chkMsg.addClass("text-red-500")
                         .text("서버 오류: 응답 형식이 잘못되었습니다.");
                    return;
                }

				if (data.rsCode.startsWith("S")) {
					chkMsg.removeClass('text-red-500').addClass('text-green-500');
					chkMsg.text(data.rsMsg);
					validLoginId = el.value;
				} else {
					chkMsg.removeClass('text-green-500').addClass('text-red-500');
					chkMsg.text(data.rsMsg);
					validLoginId = null;
				}
			}
		});
	}
</script>

<section class="mt-8">
	<div class="container mx-auto">

		<form action="/usr/member/doJoin" method="post" onsubmit="return joinFormSubmit(this);">
			<div class="table-box">
				<table class="table">

					<!-- 아이디 -->
					<tr>
						<th>아이디</th>
						<td>
							<input class="input input-neutral" name="loginId" type="text"
								   onblur="loginIdDupChk(this);" />
							<div id="chkMsg" class="mt-2 text-sm h-5"></div>
						</td>
					</tr>

					<!-- 비밀번호 -->
					<tr>
						<th>비밀번호</th>
						<td>
							<input class="input input-neutral" name="loginPw" type="password" />
							<div id="pwMsg" class="text-red-500 text-sm h-5 mt-1"></div>
						</td>
					</tr>

					<!-- 비밀번호 확인 -->
					<tr>
						<th>비밀번호 확인</th>
						<td>
							<input class="input input-neutral" name="loginPwChk" type="password" />
							<div id="pwChkMsg" class="text-red-500 text-sm h-5 mt-1"></div>
						</td>
					</tr>

					<!-- 이름 -->
					<tr>
						<th>이름</th>
						<td>
							<input class="input input-neutral" name="name" type="text" />
							<div id="nameMsg" class="text-red-500 text-sm h-5 mt-1"></div>
						</td>
					</tr>

					<!-- 닉네임 -->
					<tr>
						<th>닉네임</th>
						<td>
							<input class="input input-neutral" name="nickname" type="text" />
							<div id="nicknameMsg" class="text-red-500 text-sm h-5 mt-1"></div>
						</td>
					</tr>

					<!-- 가입 버튼 -->
					<tr>
						<td colspan="2">
							<button class="btn btn-neutral btn-outline btn-wide btn-join">가입</button>

						</td>
					</tr>

				</table>
			</div>
		</form>

		<div class="bg-white p-6">
		    <button id="backBtn"
		            class="btn btn-neutral btn-outline"
		            onclick="location.href='/usr/welcome/index';">
		        뒤로가기
		    </button>
		</div>


	</div>
</section>

<%@ include file="/view/usr/common/footer.jsp" %>
