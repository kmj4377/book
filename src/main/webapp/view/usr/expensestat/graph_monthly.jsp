<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3> 월별 지출 통계</h3>

<canvas id="monthlyChart" width="400" height="200"></canvas>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    const labels_month = [
        <c:forEach var="s" items="${monthlyStats}">
            "${s.month}",
        </c:forEach>
    ];

    const data_month = [
        <c:forEach var="s" items="${monthlyStats}">
            ${s.total},
        </c:forEach>
    ];

    new Chart(document.getElementById('monthlyChart'), {
        type: 'bar',
        data: {
            labels: labels_month,
            datasets: [{
                label: '월별 지출 금액',
                data: data_month
            }]
        }
    });
</script>
