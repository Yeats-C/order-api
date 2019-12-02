package com.aiqin.mgs.order.api.config;


import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ControllerAdvice
public class ValidationHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(ValidationHandler.class);

    /**
     * 异常处理器
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpResponse handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> objectMap = new HashMap<>();
        // 获取错误集合
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        // 初始化错误响应
        for (FieldError fieldError : fieldErrors) {
            objectMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            LOGGER.error(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return HttpResponse.failure(MessageId.create(Project.ORDER_API, 400, objectMap.toString()));
    }

    /**
     * 系统异常
     *
     * @param e
     * @param
     * @return
     */
    @ExceptionHandler(value = GroundRuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public HttpResponse handleServiceException(GroundRuntimeException e) {
        LOGGER.error(e.getMessage(), e);
        return HttpResponse.failure(MessageId.create(Project.ORDER_API, 500, e.getMessage()));
    }

    /**
     * 参数为空校验
     *
     * @param e
     * @param
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        StringBuffer sb = new StringBuffer();
        sb.append(e.getParameterName());
        sb.append("不能为空");
        return HttpResponse.failure(MessageId.create(Project.ORDER_API, 400, sb.toString()));
    }
}
