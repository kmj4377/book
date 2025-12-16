<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/header.jsp"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<title>달력</title>

<style>
body {
	background: #b38b6d;
}

.calendar-wrapper {
	width: 100%;
	max-width: 1300px;
	margin: 20px auto;
	background: #f5e8d8;
	padding: 30px;
	border-radius: 20px;
	box-shadow: 0 4px 18px rgba(0, 0, 0, 0.12);
}

.calendar-header {
	text-align: center;
	font-size: 26px;
	font-weight: 700;
	margin-bottom: 25px;
}

.calendar-header a {
	text-decoration: none;
	font-size: 22px;
	padding: 0 20px;
	color: #555;
}

.day-names {
	display: grid;
	grid-template-columns: repeat(7, 1fr);
	text-align: center;
	font-weight: 600;
	margin-bottom: 15px;
}

.day-names div:first-child {
	color: #d9534f;
}

.day-names div:last-child {
	color: #0275d8;
}

.calendar {
	display: grid;
	grid-template-columns: repeat(7, 1fr);
	gap: 12px;
}

.day {
	background: #fafafa;
	border-radius: 14px;
	padding: 14px;
	min-height: 130px;
	border: 1px solid #e5e5e5;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
	transition: .2s;
	cursor: pointer;
}

.day:hover {
	background: #eef6ff;
	transform: translateY(-4px);
}

.day.empty {
	background: #f3f3f3;
	border: none;
	cursor: default;
	box-shadow: none;
}

.today {
	background: #fff4d4 !important;
	border: 2px solid #ffcc66 !important;
}

.day-number {
	font-weight: 600;
	margin-bottom: 8px;
}

.holiday {
	color: #d9534f;
	font-weight: bold;
}

.expense-info {
	font-size: 13px;
	margin-top: 8px;
	color: #555;
}
</style>

<div class="calendar-wrapper">

	<div class="calendar-header">
		<a href="?year=${calendar.prevYear}&month=${calendar.prevMonth}">◀</a>
		${calendar.year}년 ${calendar.month}월 <a
			href="?year=${calendar.nextYear}&month=${calendar.nextMonth}">▶</a>
	</div>

	<div class="day-names">
		<div>일</div>
		<div>월</div>
		<div>화</div>
		<div>수</div>
		<div>목</div>
		<div>금</div>
		<div>토</div>
	</div>

	<div class="calendar">

		<c:forEach var="week" items="${calendar.weeks}">
			<c:forEach var="day" items="${week}">

				<c:choose>

					<c:when test="${day.day == null}">
						<div class="day empty"></div>
					</c:when>

					<c:otherwise>

						<div class="day ${day.today ? 'today' : ''}"
							onclick="location.href='/usr/expense/detail?date=${calendar.year}-${calendar.month}-${day.day}'">

							<div class="day-number ${day.holiday ? 'holiday' : ''}">
								${day.day}</div>

							<c:if test="${day.holiday}">
								<div style="font-size: 12px; color: red;">
									${day.holidayName}</div>
							</c:if>

							<div class="expense-info">
								<c:choose>
									<c:when test="${day.expenseTotal > 0}">
                                        지출: ${day.expenseTotal}원
                                    </c:when>
									<c:otherwise>
                                        지출 없음
                                    </c:otherwise>
								</c:choose>
							</div>

						</div>

					</c:otherwise>

				</c:choose>

			</c:forEach>
		</c:forEach>

	</div>

</div>

<%@ include file="../common/footer.jsp"%>