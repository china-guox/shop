package com.baidu.shop.entity;


import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "tb_sku")
@Data
public class SkuEntity {

    @Id//此处必须写long类型,因为现在新增的id已经超过int的范围了
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "商品实体主键",example = "1")
    @NotNull(message = "商品实体主键不能为空", groups = {MingruiOperation.Update.class})
    private Long id;
    @ApiModelProperty(value = "spuId",example = "1")
    @NotNull(message = "spuId不能为空", groups = {MingruiOperation.Add.class})
    private Integer spuId;
    @ApiModelProperty(value = "商品标题")
    @NotNull(message = "商品标题不能为空", groups = {MingruiOperation.Add.class})
    private String title;
    @ApiModelProperty(value = "商品图片")
    private String images;
    @ApiModelProperty(value = "销售价格，单位为分", example = "1")
    @NotEmpty(message = "销售价格不能为空", groups = {MingruiOperation.Add.class})
    private Integer price;
    @ApiModelProperty(value = "特有规格属性在spu属性模板中的对应下标组合")
    private String indexes;
    @ApiModelProperty(value = "sku的特有规格参数键值对，json格式，反序列化时请使用 linkedHashMap，保证有序")
    private String ownSpec;
    @ApiModelProperty(value = "是否有效，0无效，1有效", example = "1")
    private Integer enable;
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;
}
