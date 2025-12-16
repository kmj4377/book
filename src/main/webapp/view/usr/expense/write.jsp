<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/view/usr/common/header.jsp" />

<style>
input[type=number]::-webkit-inner-spin-button, input[type=number]::-webkit-outer-spin-button
	{
	-webkit-appearance: none;
	margin: 0;
}

input[type=number] {
	-moz-appearance: textfield;
}

.input-error {
	border-color: #ef4444 !important;
}

.input-error::placeholder {
	color: #ef4444;
}
</style>

<div class="container mx-auto px-4 max-w-lg mt-10">

	<h1 class="text-3xl font-bold mb-8 text-center">지출 등록</h1>

	<form action="/usr/expense/doWrite" method="post"
		class="flex flex-col gap-4" onsubmit="return validateExpenseForm();">

		<label class="form-control"> <span class="label-text">금액</span>
			<input type="number" id="amount" name="amount" placeholder="예: 12000"
			class="input input-bordered w-full" />
		</label>

		<label class="form-control"> <span class="label-text">카테고리</span>
			<select id="subCategoryId" name="subCategoryId"
			class="select select-bordered w-full">
				<option value="0">AI 자동 분류</option>
				<option value="1">식비</option>
				<option value="2">카페</option>
				<option value="4">교통</option>
				<option value="9">쇼핑</option>
				<option value="10">기타</option>
		</select>
		</label>

		<label class="form-control"> <span class="label-text">날짜</span>
			<input type="date" id="expenseDate" name="expenseDate"
			class="input input-bordered w-full" />
		</label>

		<label class="form-control"> <span class="label-text">메모</span>
			<textarea id="memo" name="memo"
				class="textarea textarea-bordered h-24" placeholder="예: 스타벅스 아메리카노"></textarea>
		</label>

		<button type="submit" class="btn btn-primary w-full mt-4">
			등록하기</button>
	</form>
</div>

<jsp:include page="/view/usr/common/footer.jsp" />

<script>
function validateExpenseForm() {
    const amount = document.getElementById("amount");
    const memo = document.getElementById("memo");
    const subCategory = document.getElementById("subCategoryId");

    let valid = true;

    if (!amount.value || Number(amount.value) <= 0) {
        amount.classList.add("input-error");
        amount.value = "";
        amount.placeholder = "금액을 입력해주세요";
        amount.focus();
        valid = false;
    } else {
        amount.classList.remove("input-error");
    }

    if (subCategory.value === "0" && !memo.value.trim()) {
        memo.classList.add("input-error");
        memo.placeholder = "AI 자동 분류를 사용하려면 메모를 입력해주세요";
        if (valid) memo.focus();
        valid = false;
    } else {
        memo.classList.remove("input-error");
    }

    return valid;
}

["amount", "memo"].forEach(id => {
    document.getElementById(id).addEventListener("input", e => {
        e.target.classList.remove("input-error");
    });
});
</script>
