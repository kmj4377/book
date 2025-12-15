<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- include 여부 체크 -->
<c:set var="isInclude" value="${not empty param.include}" />

<!-- include 방식이 아닐 때만 header 포함 -->
<c:if test="${!isInclude}">
    <jsp:include page="../common/header.jsp" />
</c:if>

<div class="px-6 pt-4 max-w-3xl mx-auto">

    <!-- include가 아닐 때만 제목 표시 -->
    <c:if test="${!isInclude}">
        <h2 class="text-xl font-bold mb-4">예산 대비 지출 (반원 게이지)</h2>
    </c:if>

    <!-- include가 아닐 때만 년/월 선택 -->
    <c:if test="${!isInclude}">
        <form id="searchForm" method="get" class="mb-4 flex gap-2">
            <select name="year" id="year" class="border p-1 rounded">
                <c:forEach var="y" begin="2023" end="2030">
                    <option value="${y}" ${y == param.year ? 'selected' : ''}>${y}</option>
                </c:forEach>
            </select>

            <select name="month" id="month" class="border p-1 rounded">
                <c:forEach var="m" begin="1" end="12">
                    <option value="${m}" ${m == param.month ? 'selected' : ''}>${m}</option>
                </c:forEach>
            </select>

            <button type="submit" class="bg-blue-500 text-white px-3 py-1 rounded">조회</button>
        </form>
    </c:if>

    <!-- 게이지 그래프 -->
    <div id="gaugeChart"></div>

    <!-- 상세 숫자 -->
    <div class="mt-6 p-4 bg-white rounded shadow text-center">
        <div class="text-lg font-semibold">예산: <span id="budgetAmount"></span> 원</div>
        <div class="text-lg font-semibold">지출: <span id="totalExpense"></span> 원</div>
        <div class="text-lg font-semibold text-blue-600">달성률: <span id="percent"></span>%</div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>

<script>
    // include로 값이 넘어왔는지 확인
    let year = "${param.year}";
    let month = "${param.month}";

    // include가 아니면 URL 파라미터 우선
    if (!year || year === "") {
        year = new URLSearchParams(location.search).get("year");
    }
    if (!month || month === "") {
        month = new URLSearchParams(location.search).get("month");
    }

    // 그래도 없으면 현재 날짜
    if (!year || year === "") year = new Date().getFullYear();
    if (!month || month === "") month = new Date().getMonth() + 1;

    year = parseInt(year);
    month = parseInt(month);

    loadGauge(year, month);

    function loadGauge(year, month) {
        fetch(`/usr/expenseStat/monthly?year=` + year + `&month=` + month)
            .then(res => res.json())
            .then(data => {
                if (!data.rsCode || data.rsCode.startsWith("F-")) {
                    alert(data.rsMsg || "데이터 로딩 오류");
                    return;
                }

                let info = data.rsData;

                document.getElementById("budgetAmount").innerText = info.budgetAmount.toLocaleString();
                document.getElementById("totalExpense").innerText = info.totalExpense.toLocaleString();
                document.getElementById("percent").innerText = info.percent.toFixed(1);

                drawGauge(info.percent);
            });
    }

    function drawGauge(percent) {

        var options = {
            series: [percent],
            chart: {
                height: 300,
                type: 'radialBar',
                offsetY: -20
            },
            plotOptions: {
                radialBar: {
                    startAngle: -90,
                    endAngle: 90,
                    track: {
                        background: "#fff",
                        strokeWidth: '100%',
                    },
                    dataLabels: {
                        name: { show: false },
                        value: {
                            fontSize: '28px',
                            offsetY: -10,
                            formatter: function (val) {
                                return val.toFixed(1) + "%";
                            }
                        }
                    }
                }
            },
            fill: {
                colors: ["#3b82f6"]
            }
        };

        document.querySelector("#gaugeChart").innerHTML = "";
        var chart = new ApexCharts(
            document.querySelector("#gaugeChart"),
            options
        );

        // ✅ render는 딱 한 번만
        chart.render().then(() => {
            window.dispatchEvent(new Event('resize'));
        });
    }


</script>

<!-- include 아닐 때만 footer 포함 -->
<c:if test="${!isInclude}">
    <jsp:include page="../common/footer.jsp" />
</c:if>
