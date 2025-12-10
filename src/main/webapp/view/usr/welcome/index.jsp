<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>

<style>
    body {
        margin: 0;
        font-family: 'Pretendard', sans-serif;
        height: 100vh;
        overflow: hidden;
    }

    .bg-autumn {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-image: url('/img/autumn_bg.png');
        background-size: cover;        
        background-position: center;
        background-repeat: no-repeat;
        background-color: #eacab9;       
        opacity: 0.45;
        filter: blur(3px);
        z-index: -1;
    }

    
    .center-box {
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center; 
        text-align: center;
    }

    .logo {
        width: 200px;
        margin-bottom: 20px;
    }

    .title {
        font-size: 2rem;
        font-weight: bold;
        margin-bottom: 10px;
    }

    .sub {
        font-size: 1.2rem;
        margin-bottom: 30px;
    }

    .login-btn {
        background: #b27755;   
        color: white;
        padding: 12px 24px;
        border-radius: 8px;
        text-decoration: none;
        font-size: 1.1rem;
        font-weight: bold;
        transition: 0.2s;
    }
    
    .login-btn:hover {
        background: #9c6347;
    }
    
	.join-btn {
	    width: 100%;
	    padding: 12px;
	    margin-top: 16px;
	    background: linear-gradient(135deg, rgba(255,255,255,0.9), rgba(255,255,255,0.6));
	    color: #333;
	    font-size: 16px;
	    font-weight: 600;
	    border: none;
	    border-radius: 10px;
	    backdrop-filter: blur(5px);
	    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
	    cursor: pointer;
	    transition: all 0.25s ease;
	}

	.join-btn:hover {
	    background: linear-gradient(135deg, white, rgba(255,255,255,0.85));
	    transform: translateY(-2px);
	    box-shadow: 0 8px 18px rgba(0,0,0,0.25);
	}

    

</style>

</head>

<body>
    <div class="bg-autumn"></div>

    <div class="center-box">
        <img src="/img/logo.png" class="logo" />
        
        <div class="title">환영합니다 😊</div>
        <div class="sub">가계부 서비스를 이용하려면 로그인 해주세요!</div>

        <a href="/usr/member/login" class="login-btn">로그인하기</a>
        <form method="get" action="/usr/member/join">
   	 		<button type="submit" class="join-btn">회원가입</button>
		</form>
    </div>
</body>
</html>
