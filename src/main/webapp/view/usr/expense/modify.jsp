<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/view/usr/common/header.jsp" />

<style>
/* number 스피너 제거 */
input[type=number]::-webkit-inner-spin-button,
input[type=number]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
}
input[type=number] {
    -moz-appearance: textfield;
}

/* 에러 스타일 */
.input-error {
    border-color: #ef4444 !important;
}
.error-text {
    color: #ef4444;
    font-size: 0.75rem;
    margin-top: 4px;
}
</style>

<div class="container mx-auto px-4 max-w-lg mt-10">
    <h1 class="text-3xl font-bold mb-8 text-center">지출 수정</h1>

    <form action="/usr/expense/doModify"
          method="post"
          class="flex flex-col gap-4"
          onsubmit="return validateForm();">

        <input type="hidden" name="id" value="${expense.id}" />

        <!-- 금액 -->
        <label class="form-control">
            <span class="label-text">금액</span>
            <input type="number"
                   id="amount"
                   name="amount"
                   value="${expense.amount}"
                   class="input input-bordered w-full"
                   placeholder="예: 12000" />
            <p id="amountError" class="error-text hidden">
                금액을 입력해주세요
            </p>
        </label>

        <!-- 카테고리 -->
        <label class="form-control">
            <span class="label-text">카테고리</span>
            <select name="subCategoryId" class="select select-bordered w-full">
                <option value="0">🤖 자동 분류</option>
                <option value="1"  ${expense.mainCategoryName == '식비' ? 'selected' : ''}>식비</option>
                <option value="4"  ${expense.mainCategoryName == '교통' ? 'selected' : ''}>교통비</option>
                <option value="8"  ${expense.mainCategoryName == '쇼핑' ? 'selected' : ''}>쇼핑</option>
                <option value="6"  ${expense.mainCategoryName == '생활' ? 'selected' : ''}>생활비</option>
                <option value="10" ${expense.mainCategoryName == '기타' ? 'selected' : ''}>기타</option>
            </select>
        </label>

        <!-- 날짜 -->
        <label class="form-control">
            <span class="label-text">날짜</span>
            <input type="date"
                   name="expenseDate"
                   value="${expense.expenseDate}"
                   class="input input-bordered w-full" />
        </label>

        <!-- 메모 -->
        <label class="form-control">
            <span class="label-text">메모</span>
            <textarea name="memo"
                      class="textarea textarea-bordered h-24">${expense.memo}</textarea>
        </label>

        <button type="submit" class="btn btn-primary mt-4 w-full">
            수정하기
        </button>
    </form>
</div>

<script>
function validateForm() {
    const amountInput = document.getElementById("amount");
    const errorText = document.getElementById("amountError");

    if (!amountInput.value || Number(amountInput.value) <= 0) {
        amountInput.classList.add("input-error");
        errorText.classList.remove("hidden");
        amountInput.focus();
        return false;
    }

    amountInput.classList.remove("input-error");
    errorText.classList.add("hidden");
    return true;
}
</script>

<jsp:include page="/view/usr/common/footer.jsp" />
