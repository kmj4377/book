<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="홈" />
<%@ include file="/view/usr/common/header.jsp" %>

<link rel="stylesheet" href="/css/main.css">

<div class="home-wrapper">

    <!-- 상단 유저 정보 + 검색 -->
    <div class="top-row">
        <div class="text-3xl font-bold">
            <c:if test="${not empty sessionScope.loginedMember}">
                안녕하세요 😊 
                <span class="text-blue-500">${sessionScope.loginedMember.name}</span> 님!
            </c:if>
        </div>

        <div class="flex items-center gap-3">
            <input class="input input-bordered w-64" placeholder="지출 검색..." />
            <button class="btn btn-primary">검색</button>
        </div>
    </div>

    <!-- 4개 지표 카드 -->
    <div class="card-row">

        <!-- 오늘 지출 -->
        <div class="card-box bg-red-100 text-center">
            <div class="text-sm text-gray-700">오늘 지출</div>
            <div class="text-4xl font-extrabold text-red-600">
                <fmt:formatNumber value="${todayCount}" type="number" />원
            </div>
        </div>

        <!-- 이번달 지출 -->
        <div class="card-box bg-pink-100 text-center">
            <div class="text-sm text-gray-700">이번달 지출</div>
            <div class="text-4xl font-extrabold text-pink-600">
                <fmt:formatNumber value="${monthlyExpense}" type="number" />원
            </div>
        </div>

        <!-- 이번달 예산 -->
        <div class="card-box bg-blue-100 text-center">
            <div class="text-sm text-gray-700">이번달 예산</div>
            <div class="text-4xl font-extrabold text-blue-600">
                <fmt:formatNumber value="${budget != null ? budget.amount : 0}" type="number" />원
            </div>
        </div>

        <!-- 남은 금액 -->
        <div class="card-box bg-green-100 text-center">
            <div class="text-sm text-gray-700">남은 금액</div>
            <div class="text-4xl font-extrabold text-green-600">
                <fmt:formatNumber value="${saving}" type="number" />원
            </div>
        </div>
    </div>

    <!-- 월간 지출 비교 + 도넛 차트 -->
    <div class="bottom-row">

        <!-- 월간 지출 비교 -->
        <div class="panel small-panel">
            <h2 class="text-xl font-bold mb-3">월간 지출 비교</h2>

            <div class="text-base leading-relaxed">
                <p>이번달 총 지출: 
                    <span class="font-bold text-blue-600">
                        <fmt:formatNumber value="${thisMonthTotal}" type="number"/>원
                    </span>
                </p>

                <p>지난달 총 지출:
                    <span class="font-bold text-gray-700">
                        <fmt:formatNumber value="${lastMonthTotal}" type="number"/>원
                    </span>
                </p>

                <!-- 변동률 표시 -->
                <c:choose>
                    <c:when test="${increaseRate > 0}">
                        <p class="text-red-600 font-bold mt-2">
                            ▲ <fmt:formatNumber value="${increaseRate}" type="number" maxFractionDigits="1" />% 증가
                        </p>
                    </c:when>

                    <c:when test="${increaseRate < 0}">
                        <p class="text-blue-600 font-bold mt-2">
                            ▼ <fmt:formatNumber value="${-increaseRate}" type="number" maxFractionDigits="1" />% 감소
                        </p>
                    </c:when>

                    <c:otherwise>
                        <p class="text-gray-600 mt-2">변동 없음</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <div style="text-align:center; margin-top:20px;">
                <a href="/usr/expensestat/compare" 
                   class="btn btn-primary"
                   style="padding:6px 14px; border-radius:10px; background:#4e73df; color:white; text-decoration:none;">
                    지출 추세 보기 →
                </a>
            </div>
        </div>

        <!-- 도넛 그래프 -->
        <div class="panel chart-panel">
            <h2 class="text-xl font-bold mb-2">🍩 지출 비율</h2>
            <iframe src="/usr/expensestat/category" style="width:100%; height:300px; border:0;"></iframe>
        </div>

    </div>

</div>

<%@ include file="/view/usr/common/footer.jsp" %>
