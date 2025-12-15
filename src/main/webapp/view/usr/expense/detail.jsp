<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../common/header.jsp" %>

<!-- Bootstrap Icon -->
<link rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<style>
/* 카테고리 배지 스타일 */
.category-wrap {
    display: inline-flex;
    align-items: center;
    gap: 6px;          /* ← 띄어쓰기 원인 해결 */
}

.category-main {
    padding: 4px 10px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: 600;
    background: #f3e8d8;
    color: #6b4e2e;
}

.category-sub {
    padding: 4px 10px;
    border-radius: 12px;
    font-size: 12px;
    background: #ede3d6;
    color: #7a5a36;
}
</style>

<div class="container mt-4">

    <h2 class="mb-4">지출 상세</h2>

    <table class="table table-bordered">
        <tr>
            <th style="width: 160px;">번호</th>
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

        <!-- ✅ 카테고리 -->
        <tr>
            <th>카테고리</th>
            <td>
                <div class="category-wrap">
                    <c:if test="${not empty expense.mainCategoryName}">
                        <span class="category-main">
                            ${expense.mainCategoryName}
                        </span>
                    </c:if>

                    <c:if test="${not empty expense.subCategoryName}">
                        <span class="category-sub">
                            ${expense.subCategoryName}
                        </span>
                    </c:if>
                </div>
            </td>
        </tr>

        <!-- 금액 -->
        <tr>
            <th>금액</th>
            <td>
                <strong>
                    <fmt:formatNumber value="${expense.amount}" pattern="#,###" />
                </strong> 원
            </td>
        </tr>

        <!-- 메모 -->
        <tr>
            <th>메모</th>
			<td style="white-space: pre-line;">${expense.memo}</td>
        </tr>
    </table>

    <div class="mt-3">

        <a href="/usr/expense/list" class="btn btn-secondary btn-sm">
            <i class="bi bi-list-ul"></i> 목록
        </a>

        <a href="/usr/expense/modify?id=${expense.id}" class="btn btn-primary btn-sm">
            <i class="bi bi-pencil-square"></i> 수정
        </a>

        <form action="/usr/expense/doDelete"
              method="post"
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
