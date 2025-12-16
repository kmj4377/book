<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="px-6 pt-4 max-w-4xl mx-auto">


	<div class="bg-white rounded-lg shadow p-4">
		<canvas id="monthlyTrendChart" height="120"></canvas>
	</div>

</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    const monthlyLabels = [
        <c:forEach var="row" items="${monthlyStats}">
            "${row.month}",
        </c:forEach>
    ];

    const monthlyData = [
        <c:forEach var="row" items="${monthlyStats}">
            ${row.total},
        </c:forEach>
    ];

    const ctx = document.getElementById('monthlyTrendChart').getContext('2d');

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: monthlyLabels,
            datasets: [{
                label: '월별 지출',
                data: monthlyData,
                borderWidth: 2,
                fill: false,
                borderColor: '#3b82f6',
                tension: 0.2,
                pointRadius: 4,
                pointBackgroundColor: '#3b82f6'
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString() + " 원";
                        }
                    }
                }
            }
        }
    });
</script>

