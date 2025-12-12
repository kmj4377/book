<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3>카테고리별 지출 비율</h3>

<form method="get" action="" class="ym-form">
    <div class="ym-row">
        <div class="ym-item">
            <label>년</label>
            <select name="year" onchange="this.form.submit()">
                <c:forEach var="y" begin="2020" end="2030">
                    <option value="${y}" ${y == selectedYear ? 'selected' : ''}>${y}</option>
                </c:forEach>
            </select>
        </div>

        <div class="ym-item">
            <label>월</label>
            <select name="month" onchange="this.form.submit()">
                <c:forEach var="m" begin="1" end="12">
                    <option value="${m}" ${m == selectedMonth ? 'selected' : ''}>${m}월</option>
                </c:forEach>
            </select>
        </div>
    </div>
</form>



<!-- 차트 영역 (스크롤 안생기도록 높이 고정) -->
<div class="category-chart-box">
    <canvas id="categoryChart"></canvas>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    const category_labels = [
        <c:forEach var="c" items="${categoryStats}">
            "${c.category}",
        </c:forEach>
    ];

    const category_values = [
        <c:forEach var="c" items="${categoryStats}">
            ${c.total},
        </c:forEach>
    ];

    new Chart(document.getElementById('categoryChart'), {
        type: 'doughnut',
        data: {
            labels: category_labels,
            datasets: [{
                data: category_values
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, 
            cutout: '60%',              
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        boxWidth: 12,
                        padding: 10
                    }
                }
            },
            layout: {
                padding: {
                    top: 0,
                    bottom: 0
                }
            }
        }
    });

</script>
