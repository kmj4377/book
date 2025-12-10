<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/view/usr/common/header.jsp" />

<div class="container mx-auto px-4 max-w-lg mt-10">
    <h1 class="text-3xl font-bold mb-8 text-center">지출 수정</h1>

    <form action="/usr/expense/doModify" method="post" class="flex flex-col gap-4">

        <!-- 지출 ID -->
        <input type="hidden" name="id" value="${expense.id}" />

        <!-- 금액 -->
        <label class="form-control">
            <span class="label-text">금액</span>
            <input type="number" name="amount" required
                   value="${expense.amount}"
                   class="input input-bordered w-full" />
        </label>

        <!-- 카테고리 -->
        <label class="form-control">
            <span class="label-text">카테고리</span>

            <select name="category" class="select select-bordered w-full">

                <option value="식비"    <c:if test="${expense.category == '식비'}">selected</c:if> >식비</option>
                <option value="교통비"  <c:if test="${expense.category == '교통비'}">selected</c:if> >교통비</option>
                <option value="쇼핑"    <c:if test="${expense.category == '쇼핑'}">selected</c:if> >쇼핑</option>
                <option value="생활비"  <c:if test="${expense.category == '생활비'}">selected</c:if> >생활비</option>
                <option value="기타"    <c:if test="${expense.category == '기타'}">selected</c:if> >기타</option>

            </select>
        </label>

        <!-- 날짜 -->
        <label class="form-control">
            <span class="label-text">날짜</span>
            <input type="date" name="expenseDate"
                   value="<fmt:formatDate value='${expense.expenseDate}' pattern='yyyy-MM-dd'/>"
                   class="input input-bordered w-full" />
        </label>

        <!-- 메모 -->
        <label class="form-control">
            <span class="label-text">메모</span>
            <textarea name="memo" class="textarea textarea-bordered h-24">${expense.memo}</textarea>
        </label>

        <!-- 버튼 -->
        <button type="submit" class="btn btn-primary mt-4 w-full">
            수정하기
        </button>

    </form>
</div>

<jsp:include page="/view/usr/common/footer.jsp" />
