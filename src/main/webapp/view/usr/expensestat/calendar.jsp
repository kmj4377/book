<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp" />

<style>
    .calendar-container {
        width: 800px;
        margin: 0 auto;
    }
    table.calendar {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    table.calendar th, table.calendar td {
        width: 14%;
        height: 100px;
        border: 1px solid #ccc;
        vertical-align: top;
        padding: 5px;
    }
    .today {
        background: #ffeaa7 !important;
    }
    .day-number {
        font-weight: bold;
    }
</style>

<div class="calendar-container">

    <div class="calendar-nav">
        <a href="/usr/calendar/index?year=${calendar.prevYear}&month=${calendar.prevMonth}">◀ 이전달</a>

        <h2>${calendar.year}년 ${calendar.month}월</h2>

        <a href="/usr/calendar/index?year=${calendar.nextYear}&month=${calendar.nextMonth}">다음달 ▶</a>
    </div>

    <table class="calendar">
        <thead>
            <tr>
                <th>일</th>
                <th>월</th>
                <th>화</th>
                <th>수</th>
                <th>목</th>
                <th>금</th>
                <th>토</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="week" items="${calendar.weeks}">
                <tr>
                    <c:forEach var="day" items="${week}">
                        <td class="${day.today ? 'today' : ''}">
                            <c:if test="${!day.empty}">
                                <div class="day-number">
                                    <a href="/usr/expense/write?date=${calendar.year}-${calendar.month}-${day.day}">
                                        ${day.day}
                                    </a>
                                </div>
                            </c:if>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</div>

<jsp:include page="../common/footer.jsp" />
