package com.example.todo.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {

        int statusCode = 500;
        String errorMessage = "予期せぬエラーが発生しました。時間をおいて再度お試しください。";

        // --- 条件分岐で判定 ---
        if (ex instanceof AccessDeniedException) {
            statusCode = 403;
            errorMessage = "この操作は許可されていません。";
        } else if (ex instanceof TodoNotFoundException) {
            statusCode = 404;
            errorMessage = ex.getMessage();
        } else if (ex instanceof NoResourceFoundException) {
            statusCode = 404;
            errorMessage = "ページが見つかりませんでした。";
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            statusCode = 400;
            errorMessage = "リクエストが不正です。";
        } else if (ex instanceof ResponseStatusException statusEx) {
            statusCode = statusEx.getStatusCode().value();
            errorMessage = statusEx.getReason() != null ? statusEx.getReason() : "不正な操作です。";
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);

        logger.warn("⚠️ エラーハンドリング：statusCode={}, message={}", statusCode, ex.getMessage(), ex);

        return "error/error"; // 共通テンプレート（error.htmlなど）
    }
}
