<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp"/>

<h2 class="text-xl font-bold px-4">📊 지출 통계 모음</h2>

<style>
    /* 전체 그래프를 가로로 배치 */
    .stats-row {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        justify-content: center;
        margin: 20px auto;
        max-width: 1200px;
    }

    /* 각 그래프 카드 */
    .stat-card {
        flex: 1 1 420px;
        max-width: 500px;
        border: 1px solid #ddd;
        border-radius: 10px;
        padding: 15px;
        background: #fff;
        box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .chart-wrapper {
        width: 100%;
        height: 340px;
        position: relative;
    }
</style>

<div class="stats-row">

    <!-- 1️⃣ 월별 지출 그래프 -->
    <div class="stat-card">
        <div class="chart-wrapper">
            <jsp:include page="graph_monthly.jsp">
                <jsp:param name="year" value="${selectedYear}" />
                <jsp:param name="month" value="${selectedMonth}" />
            </jsp:include>
        </div>
    </div>

    <!-- 2️⃣ 월별 카테고리 도넛 그래프 -->
    <div class="stat-card">
        <div class="chart-wrapper">
            <jsp:include page="graph_category.jsp">
                <jsp:param name="year" value="${selectedYear}" />
                <jsp:param name="month" value="${selectedMonth}" />
            </jsp:include>
        </div>
    </div>

    <!-- 3️⃣ 일별 그래프 -->
    <div class="stat-card">
        <div class="chart-wrapper">
            <jsp:include page="graph_day.jsp">
                <jsp:param name="year" value="${selectedYear}" />
                <jsp:param name="month" value="${selectedMonth}" />
            </jsp:include>
        </div>
    </div>

<!-- 4️⃣ 예산 대비 지출 반원 게이지 -->
<div class="stat-card">
    <div class="chart-wrapper">
        <jsp:include page="../budgetstat/gauge.jsp">
            <jsp:param name="include" value="true" />
            <jsp:param name="year" value="${selectedYear}" />
            <jsp:param name="month" value="${selectedMonth}" />
        </jsp:include>
    </div>
</div>


</div>

<jsp:include page="../common/footer.jsp"/>
