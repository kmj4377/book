<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="expense-ratio-content">

	<form method="get" class="expense-filter">
		<div class="filter-item">
			<select name="year" onchange="this.form.submit()">
				<c:forEach var="y" begin="2020" end="2035">
					<option value="${y}" ${y == selectedYear ? 'selected' : ''}>
						${y}년</option>
				</c:forEach>
			</select>
		</div>

		<div class="filter-item">
			<select name="month" onchange="this.form.submit()">
				<c:forEach var="m" begin="1" end="12">
					<option value="${m}" ${m == selectedMonth ? 'selected' : ''}>
						${m}월</option>
				</c:forEach>
			</select>
		</div>
	</form>

	<div class="chart-area">
		<div class="chart-container">
			<canvas id="categoryChart"></canvas>
		</div>
	</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
const AUTO_COLORS = [
    '#4EA5F5', 
    '#FF6B8A', 
    '#FFA94D', 
    '#69DB7C', 
    '#B197FC', 
    '#FFD43B'  
];

const labels = [
    <c:forEach var="c" items="${categoryStats}">
        "${c.category}",
    </c:forEach>
];

const values = [
    <c:forEach var="c" items="${categoryStats}">
        ${c.total},
    </c:forEach>
];

const colors = labels.map((_, i) => AUTO_COLORS[i % AUTO_COLORS.length]);

new Chart(document.getElementById('categoryChart'), {
    type: 'doughnut',
    data: {
        labels: labels,
        datasets: [{
            data: values,
            backgroundColor: colors,   
            borderWidth: 0,
            hoverBorderWidth: 0
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        radius: '85%',
        cutout: '65%',
        plugins: {
            legend: {
                position: 'bottom',
                labels: {
                    boxWidth: 10,
                    padding: 6,
                    font: { size: 11 }
                }
            }
        }
    }
});
</script>
