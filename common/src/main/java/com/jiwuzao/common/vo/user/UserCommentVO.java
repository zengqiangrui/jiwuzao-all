package com.jiwuzao.common.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserCommentVO {
    /**
     * 商品gid
     */
    private String gid;
    /**
     * 商品名
     */
    private String title;
    /**
     * 评论时间
     */
    private Long time;
    /**
     * 商品封面
     */
    private String cover;
    /**
     * 评论内容
     */
    private String content;
}
