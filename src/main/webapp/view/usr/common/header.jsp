<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
         

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${pageTitle}" default="소셜 가계부" /></title>


    <link rel="stylesheet" href="/css/common.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    <link href="https://cdn.jsdelivr.net/npm/daisyui@5" rel="stylesheet" />
</head>

<body>

<div class="flex min-h-screen">

    <div class="w-64 p-4 border-r flex flex-col bg-base-100">
        <h2 class="text-xl font-bold mb-4">소셜 가계부</h2>

        <ul class="menu">

            <c:if test="${empty sessionScope.loginedMember}">
                <li><a href="/usr/member/join">회원가입</a></li>
                <li><a href="/usr/member/login">로그인</a></li>
            </c:if>

            <c:if test="${not empty sessionScope.loginedMember}">
                <li><a href="/usr/budget/set">예산 설정</a></li>
                <li><a href="/usr/expense/write">지출등록</a></li>
                <li><a href="/usr/expense/list">지출 리스트</a></li>
                <li><a href="/usr/expense/calendar">달력 보기</a></li>
                <li><a href="/usr/expensestat/monthly">월별 통계</a></li>
                <li><a href="/usr/member/logout">로그아웃</a></li>
            </c:if>
        </ul>

        <div class="flex-grow"></div>

        <div class="mt-6 flex justify-center mb-12">
		    <a href="/usr/home/main" style="display: block;">
		        <img src="/img/logo.png" class="w-32 h-32 opacity-90 cursor-pointer" />
		    </a>
		</div>	
    </div>


    <div class="flex-1 flex flex-col">

        <!-- 상단바 -->
        <div class="h-20 flex items-center justify-between px-6 text-xl border-b top-bar">

            <ul class="flex header-menu gap-6 items-center ml-auto">

                <c:if test="${empty sessionScope.loginedMember}">
                    <li><a class="px-3" href="/usr/member/join">회원가입</a></li>
                    <li><a class="px-3" href="/usr/member/login">로그인</a></li>
                </c:if>

                <c:if test="${not empty sessionScope.loginedMember}">
                    <li>
                        <a class="flex items-center px-3" href="/usr/member/myPage">
                            <img src="/img/user-icon.png" style="height: 28px;">
                        </a>
                    </li>
                </c:if>

            </ul>
        </div>