<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<%@ include file="../common/header.jsp" %>

<!-- Bootstrap Icon CDN (선택기능: 아이콘 사용 시 필요) -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<div class="container mt-4">

    <h2 class="mb-4">지출 상세</h2>

    <table class="table table-bordered">
        <tr>
            <th>번호</th>
            <td>${expense.id}</td>
        </tr>
        <tr>
            <th>등록일</th>
            <td>${expense.regDate}</td>
        </tr>
        <tr>
            <th>수정일</th>
            <td>${expense.updateDate}</td>
        </tr>
        <tr>
            <th>지출 날짜</th>
            <td>${expense.expenseDate}</td>
        </tr>

        <!-- 카테고리 배지 -->
        <tr>
            <th>카테고리</th>
            <td>
                    ${expense.category}
            </td>
        </tr>

        <!-- 금액 천단위 콤마 적용 -->
        <tr>
            <th>금액</th>
            <td>
                <strong>
                    <fmt:formatNumber value="${expense.amount}" pattern="#,###" />
                </strong> 원
            </td>
        </tr>

        <!-- 메모 줄바꿈 적용 -->
        <tr>
            <th>메모</th>
            <td style="white-space: pre-line;">${expense.memo}</td>
        </tr>
    </table>

    <div class="mt-3">

        <!-- 목록 버튼 -->
        <a href="/usr/expense/list" class="btn btn-secondary btn-sm">
            <i class="bi bi-list-ul"></i> 목록
        </a>

        <!-- 수정 버튼 -->
        <a href="/usr/expense/modify?id=${expense.id}" class="btn btn-primary btn-sm">
            <i class="bi bi-pencil-square"></i> 수정
        </a>

        <!-- 삭제 버튼 -->
        <form action="/usr/expense/doDelete" method="post"
              style="display:inline;"
              onsubmit="return confirm('정말 삭제하시겠습니까?');">
            <input type="hidden" name="id" value="${expense.id}">
            <button class="btn btn-danger btn-sm">
                <i class="bi bi-trash"></i> 삭제
            </button>
        </form>
    </div>

</div>

<%@ include file="../common/footer.jsp" %>
