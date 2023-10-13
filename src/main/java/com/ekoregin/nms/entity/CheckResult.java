package com.ekoregin.nms.entity;

public class CheckResult {
    private Long id;
    private CheckResultStatus status;
    private String result;
}

enum CheckResultStatus {
    OK, ERROR, WARNING
}