<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="홈" />
<%@ include file="/view/usr/common/header.jsp"%>

<link rel="stylesheet" href="/css/main.css">

<div class="home-wrapper">

	<div class="top-row">
		<div class="text-3xl font-bold">
			<c:if test="${not empty sessionScope.loginedMember}">
                안녕하세요 😊
                <span class="text-blue-500">
					${sessionScope.loginedMember.nickname} </span> 님!
            </c:if>
		</div>

		<form method="get" action="/usr/expense/list"
			class="flex items-center gap-3">
			<input type="text" name="keyword" class="input input-bordered w-64"
				placeholder="지출 검색..." />
			<button type="submit" class="btn btn-primary">검색</button>
		</form>
	</div>

	<div class="card-row">
		<div class="card-box bg-red-100 text-center">
			<div class="text-sm text-gray-700">오늘 지출</div>
			<div class="text-4xl font-extrabold text-red-600">
				<fmt:formatNumber value="${todayCount}" />
				원
			</div>
		</div>

		<div class="card-box bg-pink-100 text-center">
			<div class="text-sm text-gray-700">이번달 지출</div>
			<div class="text-4xl font-extrabold text-pink-600">
				<fmt:formatNumber value="${monthlyExpense}" />
				원
			</div>
		</div>

		<div class="card-box bg-blue-100 text-center">
			<div class="text-sm text-gray-700">이번달 예산</div>
			<div class="text-4xl font-extrabold text-blue-600">
				<fmt:formatNumber value="${budget != null ? budget.amount : 0}" />
				원
			</div>
		</div>

		<div class="card-box bg-green-100 text-center">
			<div class="text-sm text-gray-700">남은 금액</div>
			<div class="text-4xl font-extrabold text-green-600">
				<fmt:formatNumber value="${saving}" />
				원
			</div>
		</div>
	</div>

	<div class="bottom-row">

		<!-- ① 월간 지출 비교 + 소비 요약 -->
		<div class="panel small-panel">
			<h2 class="text-xl font-bold mb-3">월간 지출 비교</h2>

			<p>
				이번달 총 지출: <strong class="text-blue-600"> <fmt:formatNumber
						value="${thisMonthTotal}" />원
				</strong>
			</p>

			<p>
				지난달 총 지출: <strong> <fmt:formatNumber
						value="${lastMonthTotal}" />원
				</strong>
			</p>

			<c:choose>
				<c:when test="${increaseRate > 0}">
					<p class="text-red-600 font-bold mt-2">
						▲
						<fmt:formatNumber value="${increaseRate}" maxFractionDigits="1" />
						%
					</p>
				</c:when>
				<c:when test="${increaseRate < 0}">
					<p class="text-blue-600 font-bold mt-2">
						▼
						<fmt:formatNumber value="${-increaseRate}" maxFractionDigits="1" />
						%
					</p>
				</c:when>
			</c:choose>

			<hr style="margin: 16px 0;" />

			<div class="text-sm">
				<strong>이번 달 소비 요약</strong>
				<ul>
					<li>• ${topCategory} 지출이 가장 커요</li>
					<li>• 예산 대비 <fmt:formatNumber
							value="${(thisMonthTotal * 100.0) / budget.amount}"
							maxFractionDigits="1" />% 사용 중
					</li>
				</ul>
			</div>

			<div class="text-center mt-3">
				<a href="/usr/expensestat/compare" class="btn btn-primary"> 지출
					추세 보기 → </a>
			</div>
		</div>

		<div class="panel small-panel">
			<h2 class="text-xl font-bold mb-3">오늘 지출 요약</h2>

			<c:choose>
				<c:when test="${empty todayExpenseList}">
					<p class="text-gray-500">오늘은 아직 지출이 없어요 😊</p>
				</c:when>

				<c:otherwise>
					<p class="mb-2">
						오늘은 <strong>${fn:length(todayExpenseList)}건</strong>, <strong>
							<fmt:formatNumber value="${todayCount}" />원
						</strong> 사용했어요
					</p>

					<ul class="text-sm text-gray-700">
						<c:forEach var="e" items="${todayExpenseList}" begin="0" end="2">
							<li>• ${e.mainCategoryName} - ${e.subCategoryName} <c:if
									test="${not empty e.memo}">
						            (${e.memo})
						        </c:if> : <fmt:formatNumber value="${e.amount}" />원
							</li>
						</c:forEach>


						<c:if test="${fn:length(todayExpenseList) > 3}">
							<li class="text-gray-400">… 외 ${fn:length(todayExpenseList) - 3}건
							</li>
						</c:if>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="panel chart-panel">
			<h2 class="text-xl font-bold mb-2">지출 비율</h2>
			<c:import url="/usr/expensestat/category" />
		</div>

	</div>
</div>

<%@ include file="/view/usr/common/footer.jsp"%>
