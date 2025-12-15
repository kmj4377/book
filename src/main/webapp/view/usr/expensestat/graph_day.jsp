<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<canvas id="dayChart"></canvas>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    // day_labels : 일자
    const day_labels = [
        <c:forEach var="d" items="${dailyStats}">
            "${d.day}",
        </c:forEach>
    ];

    // day_values : 지출 금액
    const day_values = [
        <c:forEach var="d" items="${dailyStats}">
            ${d.total},
        </c:forEach>
    ];

    console.log("day_labels =", day_labels);
    console.log("day_values =", day_values);

    new Chart(document.getElementById('dayChart'), {
        type: 'line',
        data: {
            labels: day_labels,
            datasets: [{
                label: '일자별 지출',
                data: day_values,
                borderWidth: 2,
                fill: false,
                tension: 0.2
            }]
        }
    });
</script>
