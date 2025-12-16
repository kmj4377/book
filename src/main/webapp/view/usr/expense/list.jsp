<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ include file="../common/header.jsp"%>

<c:if test="${param.msg == 'created'}">
	<script>
		alert('지출이 등록되었습니다.');
	</script>
</c:if>

<c:if test="${param.msg == 'deleted'}">
	<script>
		alert('지출이 삭제되었습니다.');
	</script>
</c:if>

<style>
h2 {
	font-size: 32px;
	font-weight: 700;
}
</style>

<div class="container mt-4">
	<h2 class="mb-4">지출 내역</h2>

	<div class="mb-3 text-end">
		<a href="/usr/expense/write" class="btn btn-primary btn-sm">지출 추가</a>
	</div>

	<c:if test="${empty expenses}">
		<div class="alert alert-info">아직 등록된 지출이 없습니다.</div>
	</c:if>

	<c:if test="${not empty expenses}">
		<table class="table table-bordered table-hover text-center">
			<thead class="table-light">
				<tr>
					<th>번호</th>
					<th>날짜</th>
					<th>카테고리</th>
					<th>금액</th>
					<th>메모</th>
					<th style="width: 180px;">관리</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach var="ex" items="${expenses}">
					<tr>
						<td>${ex.id}</td>
						<td>${ex.expenseDate}</td>
						<td>${ex.mainCategoryName}- ${ex.subCategoryName}</td>

						<td><strong> <fmt:formatNumber value="${ex.amount}"
									pattern="#,###" />
						</strong> 원</td>

						<td>${ex.memo}</td>

						<td class="text-center"><a
							href="/usr/expense/detail?id=${ex.id}"
							class="btn btn-info btn-sm mb-1"> 상세보기 </a>

							<form action="/usr/expense/doDelete" method="post"
								onsubmit="return confirm('정말 삭제하시겠습니까?');"
								style="display: inline;">
								<input type="hidden" name="id" value="${ex.id}">
								<button class="btn btn-danger btn-sm">삭제</button>
							</form></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

</div>

<%@ include file="../common/footer.jsp"%>
