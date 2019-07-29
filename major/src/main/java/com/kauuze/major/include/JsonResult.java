package com.kauuze.major.include;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用于api返回restful
 *
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
@Data
@AllArgsConstructor
public class JsonResult {
    /**
     * 业务状态字符串:failure,success。 --当http状态码为200时必含
     * success--业务成功 failure--业务失败
     */
    private String state;
    /**
     * 业务成功返回数据
     */
    private Object data;
    /**
     * 业务失败返回提示
     */
    private String message;

    /**
     * 业务成功
     *
     * @return
     */
    public static JsonResult success() {
        JsonResult jsonResult = new JsonResult("success", null, null);
        return jsonResult;
    }

    /**
     * 业务成功
     *
     * @param data
     * @return
     */
    public static JsonResult success(Object data) {
        JsonResult jsonResult = new JsonResult("success", data, null);
        return jsonResult;
    }

    /**
     * 业务失败
     *
     * @return
     */
    public static JsonResult failure() {
        JsonResult jsonResult = new JsonResult("failure", null, "操作失败");
        return jsonResult;
    }

    /**
     * 业务失败
     *
     * @param message
     * @return
     */
    public static JsonResult failure(String message) {
        if (StringUtil.isBlank(message)) {
            message = "操作失败";
        }
        JsonResult jsonResult = new JsonResult("failure", null, message);
        return jsonResult;
    }


}
