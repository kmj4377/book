<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/header.jsp"%>

<div class="container" style="max-width: 600px; margin: 30px auto;">
	<h2 style="text-align: center; margin-bottom: 30px;">예산 설정</h2>

	<form action="/usr/budget/doSet" method="post">

		<div style="margin-bottom: 20px;">
			<label>월 선택</label> <input type="month" name="month"
				class="form-control" value="${month}" required>
		</div>

		<div style="margin-bottom: 20px;">
			<label>예산 금액</label> <input type="number" name="amount"
				class="form-control" value="${budget.amount}"
				placeholder="예산 금액을 입력하세요" required>
		</div>

		<button type="submit" class="btn btn-primary"
			style="width: 100%; padding: 10px;">저장하기</button>

	</form>

</div>

<%@ include file="../common/footer.jsp"%>
