package hecc.pay.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Auther xuhoujun
 * @Description: 基础控制器
 * @Date: Created In 下午11:53 on 2018/3/2.
 */
public abstract class BaseController {
    protected static final int ERROR_CODE_VALID_FAILED = 1000;
    protected static final int ERROR_CODE_CREATE_CODE_FAILED = 2000;
    protected static final int ERROR_CODE_PAY_CODE_FAILED = 3000;
    protected static final int ERROR_WITHDRAW_FEE_LIMIT = 1000;


    protected ResponseVO succeed(Object data) {
        ResponseVO result = new ResponseVO();
        result.msg = "OK";
        result.code = 0;
        result.data = data;
        return result;
    }

    protected ResponseVO failed(String msg, int code) {
        ResponseVO result = new ResponseVO();
        result.msg = msg;
        result.code = code;
        result.data = null;
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseVO validErrorHandler(MethodArgumentNotValidException e) {
        return failed(e.getMessage(), ERROR_CODE_VALID_FAILED);
    }

    public static class ResponseVO {

        public String msg;
        public int code;
        public Object data;

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
