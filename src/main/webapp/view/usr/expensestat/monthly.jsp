<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../common/header.jsp" />

<h2 class="text-xl font-bold px-4">📊 지출 통계 모음</h2>

<style>
.stats-row {
	display: flex;
	flex-wrap: wrap;
	gap: 20px;
	justify-content: center;
	margin: 20px auto;
	max-width: 1200px;
	padding-bottom: 40px;
}

.stat-card {
	flex: 1 1 420px;
	max-width: 500px;
	border-radius: 12px;
	padding: 18px 15px 15px;
	background: #f5e8d8;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
	display: flex;
	flex-direction: column;
	align-items: center;
}

.stat-title {
	font-size: 16px;
	font-weight: 700;
	color: #4A2E1F;
	margin-bottom: 12px;
}

.chart-wrapper {
	width: 100%;
	height: 340px;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}

.monthly-stat-page .expense-filter {
	display: flex !important;
	gap: 10px;
	justify-content: center;
	margin-bottom: 16px;
}

.monthly-stat-page .expense-filter .filter-item {
	width: auto !important;
	flex: none !important;
}

.monthly-stat-page .expense-filter select {
	width: auto !important;
	min-width: 90px !important;
	height: 36px;
	padding: 6px 14px;
	background-color: #f2d5b5;
	border: 1px solid #b99577;
	border-radius: 10px;
	font-size: 14px;
	font-weight: 600;
	color: #4A2E1F;
	appearance: none;
	cursor: pointer;
}

.monthly-stat-page .expense-ratio-content .chart-container {
	width: 220px !important;
	height: 220px !important;
}

.monthly-stat-page .expense-ratio-content canvas {
	width: 100% !important;
	height: 100% !important;
}
</style>

<div class="stats-row monthly-stat-page">

	<div class="stat-card">
		<div class="stat-title">월별 지출 통계</div>
		<div class="chart-wrapper">
			<jsp:include page="graph_monthly.jsp">
				<jsp:param name="year" value="${selectedYear}" />
				<jsp:param name="month" value="${selectedMonth}" />
			</jsp:include>
		</div>
	</div>

	<div class="stat-card">
		<div class="stat-title">카테고리별 지출 비율</div>
		<div class="chart-wrapper">
			<jsp:include page="graph_category.jsp">
				<jsp:param name="year" value="${selectedYear}" />
				<jsp:param name="month" value="${selectedMonth}" />
			</jsp:include>
		</div>
	</div>

	<div class="stat-card">
		<div class="stat-title">일자별 지출</div>
		<div class="chart-wrapper">
			<jsp:include page="graph_day.jsp">
				<jsp:param name="year" value="${selectedYear}" />
				<jsp:param name="month" value="${selectedMonth}" />
			</jsp:include>
		</div>
	</div>

	<div class="stat-card">
		<div class="stat-title">예산 대비 지출 현황</div>
		<div class="chart-wrapper">
			<jsp:include page="../budgetstat/gauge.jsp">
				<jsp:param name="include" value="true" />
				<jsp:param name="year" value="${selectedYear}" />
				<jsp:param name="month" value="${selectedMonth}" />
			</jsp:include>
		</div>
	</div>

</div>

<jsp:include page="../common/footer.jsp" />
