<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>달력</title>

<style>
    body {
        margin: 0;
        font-family: "Pretendard", sans-serif;
        background: #f0ebe4;
    }

    .calendar-container {
        max-width: 1100px;
        width: 95%;
        margin: 40px auto;
        background: white;
        padding: 35px;
        border-radius: 24px;
        box-shadow: 0 6px 25px rgba(0,0,0,0.12);
    }

    .calendar-header {
        text-align: center;
        font-size: 26px;
        font-weight: 700;
        margin-bottom: 25px;
    }
    .calendar-header a {
        padding: 0 20px;
        text-decoration: none;
        font-size: 22px;
        color: #444;
    }

    table.calendar {
        width: 100%;
        border-collapse: collapse;
        table-layout: fixed; /* 칸 균등 정렬 */
        background: white;
    }

    table.calendar th {
        height: 48px;
        font-size: 17px;
        border-bottom: 2px solid #e0e0e0;
        background: #fafafa;
    }

    table.calendar td {
        height: 120px;
        padding: 10px;
        vertical-align: top;
        border: 1px solid #eee;
        position: relative;
        font-size: 15px;
        background: #fff;
        transition: 0.2s;
    }

    .not-this-month {
        background: #f4f4f4;
        color: #aaa;
    }

    .today {
        background: #fff7dd !important;
        border: 2px solid #ffc555 !important;
    }

    .expense {
        font-size: 13px;
        margin-top: 6px;
        color: #333;
        word-break: keep-all;
    }

</style>
</head>

<body>

<div class="calendar-container">

    <!-- 달력 상단 헤더 -->
    <div class="calendar-header">
        <a href="?year=${calendar.prevYear}&month=${calendar.prevMonth}">◀</a>
        ${calendar.year}년 ${calendar.month}월
        <a href="?year=${calendar.nextYear}&month=${calendar.nextMonth}">▶</a>
    </div>

    <!-- 달력 테이블 -->
    <table class="calendar">
        <thead>
            <tr>
                <th style="color:#d9534f;">일</th>
                <th>월</th>
                <th>화</th>
                <th>수</th>
                <th>목</th>
                <th>금</th>
                <th style="color:#0275d8;">토</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="week" items="${calendar.weeks}">
                <tr>
                    <c:forEach var="day" items="${week}">
                        <td class="
                            <c:if test='${!day.currentMonth}'>not-this-month</c:if>
                            <c:if test='${day.today}'> today</c:if>
                        ">
                            <c:if test="${day.day != null}">
                                <div style="font-weight:bold; font-size:16px;">
                                    ${day.day}
                                </div>

                                <!-- 지출 표시 -->
                                <c:if test="${day.expenseTotal > 0}">
                                    <div class="expense">
                                        지출: ${day.expenseTotal}원
                                    </div>
                                </c:if>

                            </c:if>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>

    </table>

</div>

</body>
</html>
