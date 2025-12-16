<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="../common/header.jsp" />

<style>
.compare-form {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    flex-wrap: wrap;
    margin-bottom: 24px;
}

.compare-form select {
    width: auto;
    min-width: 120px;
    height: 44px;
    padding: 0 12px;
    border-radius: 10px;
    border: 1px solid #ddd;
    background: #fbe4c8;
}

.compare-form button {
    height: 44px;
    padding: 0 20px;
    border-radius: 12px;
    background: #8b5e3c;
    color: #fff;
    font-weight: 600;
    border: none;
    cursor: pointer;
}

.compare-form span {
    font-weight: bold;
    opacity: 0.7;
}
</style>

<div class="px-6 pt-4 w-full max-w-[1400px] mx-auto">

    <h2 class="text-xl font-bold mb-4">월별 지출 비교</h2>

    <form method="get"
      action="/usr/expensestat/compare"
      class="compare-form">

    <select name="year1">
        <c:forEach var="y" begin="2020" end="2030">
            <option value="${y}" ${y == year1 ? 'selected' : ''}>${y}년</option>
        </c:forEach>
    </select>

    <select name="month1">
        <c:forEach var="m" begin="1" end="12">
            <option value="${m}" ${m == month1 ? 'selected' : ''}>${m}월</option>
        </c:forEach>
    </select>

    <span class="vs-text">vs</span>

    <select name="year2">
        <c:forEach var="y" begin="2020" end="2030">
            <option value="${y}" ${y == year2 ? 'selected' : ''}>${y}년</option>
        </c:forEach>
    </select>

    <select name="month2">
        <c:forEach var="m" begin="1" end="12">
            <option value="${m}" ${m == month2 ? 'selected' : ''}>${m}월</option>
        </c:forEach>
    </select>

    <button type="submit">비교하기</button>
</form>


    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
        <div class="bg-[#f5e8d8] p-4 shadow rounded text-center">
            <div class="text-sm text-gray-500">${year1}년 ${month1}월</div>
            <div class="text-2xl font-bold">
                <fmt:formatNumber value="${total1}" /> 원
            </div>
        </div>

        <div class="bg-[#f5e8d8] p-4 shadow rounded text-center">
            <div class="text-sm text-gray-500">${year2}년 ${month2}월</div>
            <div class="text-2xl font-bold">
                <fmt:formatNumber value="${total2}" /> 원
            </div>
        </div>
    </div>

    <div class="bg-[#f5e8d8] p-4 shadow rounded mb-6 text-center text-lg">
        <c:choose>
            <c:when test="${diff > 0}">
                <span class="text-red-600">
                    ${year1}년 ${month1}월이
                    <fmt:formatNumber value="${diff}" />원 더 썼습니다.
                </span>
            </c:when>
            <c:when test="${diff < 0}">
                <span class="text-blue-600">
                    ${year1}년 ${month1}월이
                    <fmt:formatNumber value="${-diff}" />원 덜 썼습니다.
                </span>
            </c:when>
            <c:otherwise>
                <span class="text-gray-500">두 달 지출이 동일합니다.</span>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="flex flex-wrap gap-6 w-full justify-center">

        <div class="p-6 bg-[#f5e8d8] rounded-2xl shadow-lg min-h-[450px] flex flex-col flex-1 min-w-[330px] max-w-[450px]">
            <h3 class="text-lg font-bold mb-4">지출 비교 그래프</h3>
            <div class="flex-1 relative">
                <canvas id="barChart"></canvas>
            </div>
        </div>

        <div class="p-6 bg-[#f5e8d8] rounded-2xl shadow-lg min-h-[450px] flex flex-col flex-1 min-w-[330px] max-w-[450px]">
            <h3 class="text-lg font-bold mb-4">상위 3개 카테고리</h3>

            <div class="text-sm leading-6">
                <h4 class="font-semibold mb-2">${year1}년 ${month1}월</h4>
                <ul class="list-disc pl-5">
                    <c:forEach var="item" items="${top3Month1}">
                        <li>${item.category} :
                            <fmt:formatNumber value="${item.total}" />원
                        </li>
                    </c:forEach>
                </ul>

                <h4 class="font-semibold mt-6 mb-2">${year2}년 ${month2}월</h4>
                <ul class="list-disc pl-5">
                    <c:forEach var="item" items="${top3Month2}">
                        <li>${item.category} :
                            <fmt:formatNumber value="${item.total}" />원
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

<div class="p-6 bg-[#f5e8d8] rounded-2xl shadow-lg min-h-[450px] flex flex-col flex-1 min-w-[330px] max-w-[450px]">
    <h3 class="text-lg font-bold mb-4">소비 비중</h3>

    <div class="flex-1 flex items-center justify-center gap-10">

        <div class="flex flex-col items-center">
            <div class="mb-2 text-sm font-semibold text-gray-600">
                ${year1}년 ${month1}월
            </div>
            <div class="w-[180px] h-[180px]">
                <canvas id="doughnutChart1"></canvas>
            </div>
        </div>

        <div class="flex flex-col items-center">
            <div class="mb-2 text-sm font-semibold text-gray-600">
                ${year2}년 ${month2}월
            </div>
            <div class="w-[180px] h-[180px]">
                <canvas id="doughnutChart2"></canvas>
            </div>
        </div>

    </div>
</div>


<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
new Chart(barChart, {
    type: 'bar',
    data: {
        labels: ['${year1}-${month1}', '${year2}-${month2}'],
        datasets: [{
            label: '지출 금액',
            data: [${total1}, ${total2}],
            backgroundColor: ['#4A90E2', '#50E3C2']
        }]
    },
    options: { responsive: true, maintainAspectRatio: false }
});

new Chart(doughnutChart1, {
    type: 'doughnut',
    data: {
        labels: [<c:forEach var="c" items="${cateStat1}">"${c.category}",</c:forEach>],
        datasets: [{ data: [<c:forEach var="c" items="${cateStat1}">${c.total},</c:forEach>] }]
    },
    options: { cutout: '55%' }
});

new Chart(doughnutChart2, {
    type: 'doughnut',
    data: {
        labels: [<c:forEach var="c" items="${cateStat2}">"${c.category}",</c:forEach>],
        datasets: [{ data: [<c:forEach var="c" items="${cateStat2}">${c.total},</c:forEach>] }]
    },
    options: { cutout: '55%' }
});
</script>

<jsp:include page="../common/footer.jsp" />
