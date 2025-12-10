<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="../common/header.jsp" />

<div class="px-6 pt-4 w-full max-w-[1400px] mx-auto">

    <h2 class="text-xl font-bold mb-4">월별 지출 비교</h2>

    <!-- 선택 폼 -->
    <form method="get" action="/usr/expensestat/compare">
        <div class="flex flex-wrap gap-4 mb-6">

            <!-- 첫 번째 월 -->
            <select name="year1" class="border p-2 rounded">
                <c:forEach var="y" begin="2020" end="2030">
                    <option value="${y}" ${y eq year1 ? 'selected' : ''}>${y}년</option>
                </c:forEach>
            </select>

            <select name="month1" class="border p-2 rounded">
                <c:forEach var="m" begin="1" end="12">
                    <option value="${m}" ${m eq month1 ? 'selected' : ''}>${m}월</option>
                </c:forEach>
            </select>

            <span class="pt-2">vs</span>

            <!-- 두 번째 월 -->
            <select name="year2" class="border p-2 rounded">
                <c:forEach var="y" begin="2020" end="2030">
                    <option value="${y}" ${y eq year2 ? 'selected' : ''}>${y}년</option>
                </c:forEach>
            </select>

            <select name="month2" class="border p-2 rounded">
                <c:forEach var="m" begin="1" end="12">
                    <option value="${m}" ${m eq month2 ? 'selected' : ''}>${m}월</option>
                </c:forEach>
            </select>

            <button class="bg-blue-600 text-white px-4 py-2 rounded">비교하기</button>
        </div>
    </form>

    <!-- 금액 박스 -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
        <div class="bg-white p-4 shadow rounded text-center">
            <div class="text-sm text-gray-500">${year1}년 ${month1}월</div>
            <div class="text-2xl font-bold">
                <fmt:formatNumber value="${total1}" type="number"/> 원
            </div>
        </div>

        <div class="bg-white p-4 shadow rounded text-center">
            <div class="text-sm text-gray-500">${year2}년 ${month2}월</div>
            <div class="text-2xl font-bold">
                <fmt:formatNumber value="${total2}" type="number"/> 원
            </div>
        </div>
    </div>

    <!-- 차이 표시 -->
    <div class="bg-white p-4 shadow rounded mb-6 text-center text-lg">
        <c:choose>
            <c:when test="${diff > 0}">
                <span class="text-red-600">
                    ${year1}년 ${month1}월이 ${year2}년 ${month2}월보다 
                    <fmt:formatNumber value="${diff}" type="number"/>원 더 썼습니다.
                </span>
            </c:when>
            <c:when test="${diff < 0}">
                <span class="text-blue-600">
                    ${year1}년 ${month1}월이 ${year2}년 ${month2}월보다 
                    <fmt:formatNumber value="${-diff}" type="number"/>원 덜 썼습니다.
                </span>
            </c:when>
            <c:otherwise>
                <span class="text-gray-500">두 달 지출이 동일합니다.</span>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- 그래프 카드 -->
    <div class="flex flex-wrap gap-6 w-full justify-center">

        <!-- 막대 그래프 -->
        <div class="p-6 bg-white rounded-2xl shadow-lg min-h-[450px] flex flex-col flex-1 min-w-[330px] max-w-[450px]">
            <h3 class="text-lg font-bold mb-4">지출 비교 그래프</h3>
            <div class="flex-1 relative">
                <canvas id="barChart"></canvas>
            </div>
        </div>

        <!-- TOP 3 카테고리 -->
        <div class="p-6 bg-white rounded-2xl shadow-lg min-h-[450px] flex flex-col flex-1 min-w-[330px] max-w-[450px]">
            <h3 class="text-lg font-bold mb-4">상위 3개 카테고리</h3>

            <div class="flex-1 text-sm leading-6">

                <h4 class="font-semibold mb-2">${year1}년 ${month1}월</h4>
                <ul class="list-disc pl-5">
                    <c:forEach var="item" items="${top3Month1}">
                        <li>${item.category} :
                            <fmt:formatNumber value="${item.total}" pattern="#,###"/>원
                        </li>
                    </c:forEach>
                </ul>

                <h4 class="font-semibold mt-6 mb-2">${year2}년 ${month2}월</h4>
                <ul class="list-disc pl-5">
                    <c:forEach var="item" items="${top3Month2}">
                        <li>${item.category} :
                            <fmt:formatNumber value="${item.total}" pattern="#,###"/>원
                        </li>
                    </c:forEach>
                </ul>

            </div>
        </div>

        <!-- 도넛 그래프 두 개 -->
        <div class="p-6 bg-white rounded-2xl shadow-lg min-h-[450px] flex flex-col flex-1 min-w-[330px] max-w-[450px]">
            <h3 class="text-lg font-bold mb-4">소비 비중 도넛</h3>

            <div class="flex-1 flex items-center justify-center gap-6 flex-wrap">

                <div class="relative" style="width:181px; height:181px;">
                    <canvas id="doughnutChart1"></canvas>
                </div>

                <div class="relative" style="width:181px; height:181px;">
                    <canvas id="doughnutChart2"></canvas>
                </div>

            </div>
        </div>

    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
/* 막대 그래프 */
new Chart(document.getElementById('barChart'), {
    type: 'bar',
    data: {
        labels: ['${year1}-${month1}', '${year2}-${month2}'],
        datasets: [{
            label: '지출 금액',
            data: [${total1}, ${total2}],
            backgroundColor: ['#4A90E2', '#50E3C2'],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: { y: { beginAtZero: true } }
    }
});

/* 도넛 1 */
new Chart(document.getElementById("doughnutChart1"), {
    type: 'doughnut',
    data: {
        labels: [<c:forEach var="c" items="${cateStat1}">"${c.category}",</c:forEach>],
        datasets: [{
            data: [<c:forEach var="c" items="${cateStat1}">${c.total},</c:forEach>]
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: true,
        cutout: '55%'
    }
});

/* 도넛 2 */
new Chart(document.getElementById("doughnutChart2"), {
    type: 'doughnut',
    data: {
        labels: [<c:forEach var="c" items="${cateStat2}">"${c.category}",</c:forEach>],
        datasets: [{
            data: [<c:forEach var="c" items="${cateStat2}">${c.total},</c:forEach>]
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: true,
        cutout: '55%'
    }
});
</script>

<jsp:include page="../common/footer.jsp" />
