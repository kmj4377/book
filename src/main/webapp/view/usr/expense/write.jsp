<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/view/usr/common/header.jsp" />

<div class="container mx-auto px-4 max-w-lg mt-10">
    <h1 class="text-3xl font-bold mb-8 text-center">지출 등록</h1>

    <form action="/usr/expense/doWrite" method="post" class="flex flex-col gap-4">

        <!-- 금액 -->
        <label class="form-control">
            <span class="label-text">금액</span>
            <input type="number" name="amount" required
                   placeholder="예: 12000"
                   class="input input-bordered w-full" />
        </label>

		<!-- 카테고리 -->
		<label class="form-control">
		    <span class="label-text">카테고리</span>
		    <select name="category" required class="select select-bordered w-full">
		        <option value="">카테고리를 선택하세요</option>
		        <option value="auto">AI 자동 추천</option>
		        <option value="식비">식비</option>
		        <option value="교통비">교통비</option>
		        <option value="쇼핑">쇼핑</option>
		        <option value="생활비">생활비</option>
		        <option value="기타">기타</option>
		    </select>
		</label>


        <!-- 날짜 -->
        <label class="form-control">
            <span class="label-text">날짜</span>
            <input type="date" name="expenseDate" required class="input input-bordered w-full" />
        </label>

        <!-- 메모 -->
        <label class="form-control">
            <span class="label-text">메모</span>
            <textarea name="memo" class="textarea textarea-bordered h-24"
                      placeholder="지출에 대한 메모를 입력하세요"></textarea>
        </label>

        <button type="submit" class="btn btn-primary w-full mt-4">
            등록하기
        </button>
    </form>
</div>

<jsp:include page="/view/usr/common/footer.jsp" />
