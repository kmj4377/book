<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="회원가입" />
<%@ include file="/view/usr/common/header.jsp"%>

<style>
body {
	margin: 0;
	padding: 0;
}

.join-wrapper {
	width: 100%;
	height: calc(100vh - 80px);
	display: flex;
	justify-content: center;
	align-items: flex-start;
	padding-top: 40px;
	box-sizing: border-box;
}

.join-card {
	width: 600px;
	background: #f5e8d8;
	padding: 35px 40px;
	border-radius: 20px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.18);
}

.join-title {
	font-size: 22px;
	font-weight: bold;
	margin-bottom: 18px;
}

.input-row {
	margin-bottom: 14px;
}
</style>

<script>
	let validLoginId = null;

	let timerInterval = null;
	let timeLeft = 300; 

	function startEmailTimer() {
	    timeLeft = 300;

	    if (timerInterval !== null) clearInterval(timerInterval);

	    timerInterval = setInterval(function () {

	        if (timeLeft <= 0) {
	            clearInterval(timerInterval);
	            timerInterval = null;

	            $('#emailTimerMsg')
	                .css('color', 'red')
	                .text("인증코드가 만료되었습니다. 다시 발송해주세요.");

	            $('#emailAuthed').val("0");
	            return;
	        }

	        let min = Math.floor(timeLeft / 60);
	        let sec = timeLeft % 60;

	        $('#emailTimerMsg')
	            .css('color', 'green')
	            .text("남은 시간: " +
	                    String(min).padStart(2, '0') +
	                    "분 " +
	                    String(sec).padStart(2, '0') +
	                    "초");

	        timeLeft--;

	    }, 1000);
	}

	function sendEmailCode() {
	    let email = $('input[name=email]').val().trim();

	    if (email.length === 0) {
	        $('#emailTimerMsg').css("color", "red").text('이메일을 입력해주세요.');
	        return;
	    }

	    $('#emailTimerMsg').css("color", "black").text('인증코드 발송 중...');

	    $.ajax({
	        url: '/usr/member/sendEmailAuthCode',
	        type: 'post',
	        data: { email: email },
	        success: function(data) {

	            if (!data.rsCode.startsWith("S")) {
	                $('#emailTimerMsg').css("color", "red").text(data.rsMsg);
	                return;
	            }

	            $('#emailCodeBox').show();
	            $('#emailAuthed').val("0");

	            $('#emailTimerMsg').css('color', 'green').text('남은 시간: 05분 00초');
	            startEmailTimer();
	        },
	        error: function() {
	            $('#emailTimerMsg').text('서버 오류가 발생했습니다.');
	        }
	    });
	}

	function checkEmailCode() {
	    let email = $('input[name=email]').val().trim();
	    let code = $('#emailCode').val().trim();

	    if (code.length === 0) {
	        $('#emailTimerMsg')
	            .css("color", "red")
	            .text("인증코드를 입력해주세요.");
	        return;
	    }

	    $.ajax({
	        url: '/usr/member/checkEmailAuthCode',
	        type: 'post',
	        data: { email: email, code: code },
	        success: function(data) {


	            if (data.rsCode.startsWith("S")) {

	                clearInterval(timerInterval);
	                timerInterval = null;

	                $('#emailTimerMsg')
	                    .css("color","green")
	                    .text("이메일 인증이 완료되었습니다.");

	                $('#emailAuthed').val("1");
	                return;
	            }

	            $('#emailTimerMsg')
	                .css("color","red")
	                .text(data.rsMsg);

	            setTimeout(() => {
	                if (timerInterval !== null) {
	                    let min = Math.floor(timeLeft / 60);
	                    let sec = timeLeft % 60;

	                    $('#emailTimerMsg')
	                        .css("color","green")
	                        .text("남은 시간: "
	                            + String(min).padStart(2, '0')
	                            + "분 "
	                            + String(sec).padStart(2, '0')
	                            + "초");
	                }
	            }, 2000);
	        }
	    });
	}

	/* -----------------------------------
	   아이디 중복 검사
	----------------------------------- */
	const loginIdDupChk = function(el) {
		let chkMsg = $('#chkMsg');
		el.value = el.value.trim();

		if (el.value.length === 0) {
			chkMsg.addClass('text-red-500').text('아이디는 필수 입력 정보입니다.');
			validLoginId = null;
			return;
		}

		$.ajax({
			url: '/usr/member/loginIdDupChk',
			type: 'get',
			data: { loginId: el.value },
			dataType: 'json',
			success: function(data) {

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

	/* -----------------------------------
	   회원가입 제출
	----------------------------------- */
	const joinFormSubmit = function(form) {

		$('#pwMsg').text('');
		$('#pwChkMsg').text('');
		$('#nameMsg').text('');
		$('#nicknameMsg').text('');
		$('#emailTimerMsg').text('');

		form.loginId.value = form.loginId.value.trim();
		form.loginPw.value = form.loginPw.value.trim();
		form.loginPwChk.value = form.loginPwChk.value.trim();
		form.name.value = form.name.value.trim();
		form.nickname.value = form.nickname.value.trim();
		form.email.value = form.email.value.trim();

		if (form.loginId.value.length === 0) {
			$('#chkMsg').text('아이디는 필수 입력 정보입니다.');
			return false;
		}

		if (form.loginId.value !== validLoginId) {
			loginIdDupChk(form.loginId);
			return false;
		}

		if (form.loginPw.value.length === 0) {
			$('#pwMsg').text('비밀번호는 필수 입력 정보입니다.');
			return false;
		}

		if (form.loginPw.value !== form.loginPwChk.value) {
			$('#pwChkMsg').text('비밀번호가 일치하지 않습니다.');
			return false;
		}

		if (form.name.value.length === 0) {
			$('#nameMsg').text('이름은 필수 입력 정보입니다.');
			return false;
		}

		if (form.nickname.value.length === 0) {
			$('#nicknameMsg').text('닉네임은 필수 입력 정보입니다.');
			return false;
		}

		if (form.email.value.length === 0) {
			$('#emailTimerMsg').text('이메일은 필수 입력 정보입니다.');
			return false;
		}

		if ($('#emailAuthed').val() !== '1') {
			$('#emailTimerMsg').css("color","red").text('이메일 인증을 완료해주세요.');
			return false;
		}

		return true;
	}
</script>


<div class="join-wrapper">
	<div class="join-card">

		<div class="join-title">회원가입</div>

		<form action="/usr/member/doJoin" method="post"
			onsubmit="return joinFormSubmit(this);">

			<!-- 아이디 -->
			<div class="input-row">
				<label>아이디</label> <input class="input input-neutral w-full"
					name="loginId" type="text" onblur="loginIdDupChk(this);" />
				<div id="chkMsg" class="text-red-500 text-sm mt-1"></div>
			</div>

			<!-- 비밀번호 -->
			<div class="input-row">
				<label>비밀번호</label> <input class="input input-neutral w-full"
					name="loginPw" type="password" />
				<div id="pwMsg" class="text-red-500 text-sm mt-1"></div>
			</div>

			<!-- 비밀번호 확인 -->
			<div class="input-row">
				<label>비밀번호 확인</label> <input class="input input-neutral w-full"
					name="loginPwChk" type="password" />
				<div id="pwChkMsg" class="text-red-500 text-sm mt-1"></div>
			</div>

			<!-- 이름 -->
			<div class="input-row">
				<label>이름</label> <input class="input input-neutral w-full"
					name="name" type="text" />
				<div id="nameMsg" class="text-red-500 text-sm mt-1"></div>
			</div>

			<!-- 닉네임 -->
			<div class="input-row">
				<label>닉네임</label> <input class="input input-neutral w-full"
					name="nickname" type="text" />
				<div id="nicknameMsg" class="text-red-500 text-sm mt-1"></div>
			</div>

			<!-- 이메일 -->
			<div class="input-row">
				<label>이메일</label>

				<div class="flex gap-2">
					<input class="input input-neutral flex-1" name="email" type="text"
						placeholder="example@gmail.com">

					<button type="button" class="btn btn-neutral btn-sm"
						onclick="sendEmailCode()">인증코드 발송</button>
				</div>

				<!-- 인증코드 입력 -->
				<div id="emailCodeBox" class="mt-2 flex gap-2"
					style="display: none;">
					<input id="emailCode" class="input input-neutral flex-1"
						type="text" placeholder="인증코드 입력" />
					<button type="button" class="btn btn-neutral btn-sm"
						onclick="checkEmailCode()">확인</button>
				</div>

				<div id="emailTimerMsg" class="text-red-500 text-sm mt-2"></div>
			</div>

			<input type="hidden" id="emailAuthed" name="emailAuthed" value="0">

			<!-- 가입 버튼 -->
			<button class="btn btn-neutral btn-wide mt-4">가입</button>
		</form>

		<!-- 뒤로가기 -->
		<button class="btn btn-neutral btn-outline w-full mt-4"
			onclick="location.href='/usr/welcome/index';">뒤로가기</button>

	</div>
</div>

<%@ include file="/view/usr/common/footer.jsp"%>
