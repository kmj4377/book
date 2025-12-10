package com.example.demo.dto;

import lombok.Data;

@Data
public class ResultData<DT> {
    private String rsCode;
    private String rsMsg;
    private DT rsData;

    public ResultData(String rsCode, String rsMsg) {
        this(rsCode, rsMsg, null);
    }

    public ResultData(String rsCode, String rsMsg, DT rsData) {
        this.rsCode = rsCode;
        this.rsMsg = rsMsg;
        this.rsData = rsData;
    }

    public boolean isSuccess() {
        return this.rsCode.startsWith("S-");
    }

    public boolean isFail() {
        return !this.isSuccess();
    }

    // ---------------------------
    // 🔥 추가된 정적 메서드들
    // ---------------------------

    // (1) rsCode + rsMsg → rsData 없이 사용 가능
    public static <DT> ResultData<DT> from(String rsCode, String rsMsg) {
        return new ResultData<>(rsCode, rsMsg, null);
    }

    // (2) rsCode + rsMsg + rsData
    public static <DT> ResultData<DT> from(String rsCode, String rsMsg, DT rsData) {
        return new ResultData<>(rsCode, rsMsg, rsData);
    }

    public static <DT> ResultData<DT> success(String msg, DT data) {
        return new ResultData<>("S-1", msg, data);
    }

    public static <DT> ResultData<DT> success(String msg) {
        return new ResultData<>("S-1", msg, null);
    }

    public static <DT> ResultData<DT> fail(String msg) {
        return new ResultData<>("F-1", msg, null);
    }

    public static <DT> ResultData<DT> fail(String msg, DT data) {
        return new ResultData<>("F-1", msg, data);
    }
}
