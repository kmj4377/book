<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>일별 지출 비교</title>

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body {
            padding: 20px;
        }
        #chartBox {
            width: 100%;
            max-width: 900px;
            margin: 0 auto;
            height: 400px;
        }
    </style>
</head>

<body>

<h2 style="text-align:center;">
    ${thisYear}년 ${thisMonth}월 vs ${prevYear}년 ${prevMonth}월 지출 비교
</h2>

<div id="chartBox">
    <canvas id="dayCompareChart"></canvas>
</div>

<script>


const thisMonthRaw = [
    <c:forEach var="row" items="${thisMonthRaw}" varStatus="st">
        { 
            day: ${row['day']}, 
            amount: ${row['total'] == null ? 0 : row['total']} 
        }<c:if test="${!st.last}">,</c:if>
    </c:forEach>
];

const prevMonthRaw = [
    <c:forEach var="row" items="${prevMonthRaw}" varStatus="st">
        { 
            day: ${row['day']}, 
            amount: ${row['total'] == null ? 0 : row['total']} 
        }<c:if test="${!st.last}">,</c:if>
    </c:forEach>
];

thisMonthRaw.sort((a, b) => a.day - b.day);
prevMonthRaw.sort((a, b) => a.day - b.day);

const labels = Array.from(new Set([
    ...thisMonthRaw.map(r => r.day),
    ...prevMonthRaw.map(r => r.day)
])).sort((a, b) => a - b);

function mapAmounts(source, labels) {
    return labels.map(day => {
        const found = source.find(r => r.day === day);
        return found ? found.amount : 0;
    });
}

const thisMonthData = mapAmounts(thisMonthRaw, labels);
const prevMonthData = mapAmounts(prevMonthRaw, labels);

const ctx = document.getElementById('dayCompareChart').getContext('2d');

new Chart(ctx, {
    type: 'line',
    data: {
        labels: labels,
        datasets: [
            {
                label: "${prevYear}년 ${prevMonth}월",
                data: prevMonthData,
                fill: false,
                tension: 0.3,
                borderWidth: 2
            },
            {
                label: "${thisYear}년 ${thisMonth}월",
                data: thisMonthData,
                fill: false,
                tension: 0.3,
                borderWidth: 2
            }
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    callback: value => value.toLocaleString() + '원'
                }
            }
        },
        plugins: {
            tooltip: {
                callbacks: {
                    label: ctx => ctx.dataset.label + ': ' + ctx.raw.toLocaleString() + '원'
                }
            }
        }
    }
});
</script>

</body>
</html>
