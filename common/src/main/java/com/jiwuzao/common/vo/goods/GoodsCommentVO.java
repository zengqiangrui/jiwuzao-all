package com.jiwuzao.common.vo.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GoodsCommentVO {
    /**
     * 评论人uid
     */
    private Integer uid;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 评论时间
     */
    private Long time;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论星
     */
    private Integer star;
}
