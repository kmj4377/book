<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="이메일 인증" />
<c:set var="safeEmail" value="${empty email ? '' : email}" />

<jsp:include page="../common/header.jsp" />

<style>
.email-auth-wrapper {
	display: flex;
	justify-content: center;
	margin-top: 40px;
}

.email-auth-card {
	width: 420px;
	background: #f5e8d8;
	padding: 30px;
	border-radius: 18px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.18);
}

.email-auth-title {
	font-size: 20px;
	font-weight: bold;
	margin-bottom: 20px;
	text-align: center;
}
</style>

<script>
let timerInterval = null;
let timeLeft = 300;

function startEmailTimer() {
    timeLeft = 300;

    if (timerInterval) clearInterval(timerInterval);

    timerInterval = setInterval(() => {
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            timerInterval = null;

            $('#emailTimerMsg')
                .css('color','red')
                .text('인증코드가 만료되었습니다. 다시 발송해주세요.');
            return;
        }

        const min = Math.floor(timeLeft / 60);
        const sec = timeLeft % 60;

        $('#emailTimerMsg')
            .css('color','green')
            .text(
                '남은 시간: ' +
                String(min).padStart(2,'0') + '분 ' +
                String(sec).padStart(2,'0') + '초'
            );

        timeLeft--;
    }, 1000);
}

function sendEmailCode() {

    if ('${safeEmail}'.length === 0) {
        $('#emailTimerMsg')
            .css('color','red')
            .text('이메일 정보가 없습니다.');
        return;
    }

    $('#emailTimerMsg')
        .css('color','black')
        .text('인증코드 발송 중...');

    $.ajax({
        url: '/usr/member/sendEmailAuthCode',
        type: 'post',
        dataType: 'json',
        data: { email: '${safeEmail}' },
        success: function(res) {

            if (!res.rsCode.startsWith('S')) {
                $('#emailTimerMsg')
                    .css('color','red')
                    .text(res.rsMsg);
                return;
            }

            $('#emailCodeBox').show();
            $('#emailTimerMsg')
                .css('color','green')
                .text('남은 시간: 05분 00초');

            startEmailTimer();
        },
        error: function(xhr) {
            $('#emailTimerMsg')
                .css('color','red')
                .text(xhr.responseJSON?.msg || '오류가 발생했습니다.');
        }
    });
}

function checkEmailCode() {

    const code = $('#emailCode').val().trim();

    if (code.length === 0) {
        $('#emailTimerMsg')
            .css('color','red')
            .text('인증코드를 입력해주세요.');
        return;
    }

    $.ajax({
        url: '/usr/member/checkEmailAuthCode',
        type: 'post',
        dataType: 'json',
        data: {
            email: '${safeEmail}',
            code: code
        },
        success: function(res) {

            if (res.rsCode.startsWith('S')) {

                clearInterval(timerInterval);
                timerInterval = null;

                $('#emailTimerMsg')
                    .css('color','green')
                    .text('이메일 인증이 완료되었습니다.');

                setTimeout(() => {
                    location.href = '/usr/member/mypage';
                }, 800);

                return;
            }

            $('#emailTimerMsg')
                .css('color','red')
                .text(res.rsMsg);
        },
        error: function(xhr) {
            $('#emailTimerMsg')
                .css('color','red')
                .text(xhr.responseJSON?.msg || '인증 실패');
        }
    });
}
</script>

<div class="email-auth-wrapper">
	<div class="email-auth-card">

		<div class="email-auth-title">이메일 인증</div>

		<p style="text-align: center; margin-bottom: 16px;">
			이메일: <b>${safeEmail}</b>
		</p>

		<button class="btn btn-neutral w-full" type="button"
			onclick="sendEmailCode()">인증코드 발송</button>

		<div id="emailCodeBox" style="display: none; margin-top: 16px;">
			<input id="emailCode" type="text" class="input input-neutral w-full"
				placeholder="인증코드 입력">

			<button class="btn btn-neutral w-full mt-2" type="button"
				onclick="checkEmailCode()">확인</button>

			<div id="emailTimerMsg" class="text-sm mt-2"></div>
		</div>

	</div>
</div>

<jsp:include page="../common/footer.jsp" />
