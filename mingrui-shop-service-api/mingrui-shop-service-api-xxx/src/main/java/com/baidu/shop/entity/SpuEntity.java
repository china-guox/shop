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

@Table(name = "tb_spu")
@Data
public class SpuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", example = "1")
    @NotNull(message = "主键不能为空", groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "标题")
    @NotEmpty(message = "标题不能为空", groups = {MingruiOperation.Add.class})
    private String title;

    @ApiModelProperty(value = "子标题")
    private String subTitle;
    @ApiModelProperty(value = "1级类目id", example = "1")
    @NotNull(message = "1级类目id不能为空",groups = {MingruiOperation.Add.class})
    private Integer cid1;
    @ApiModelProperty(value = "2级类目id", example = "1")
    @NotNull(message = "2级类目id不能为空",groups = {MingruiOperation.Add.class})
    private Integer cid2;
    @ApiModelProperty(value = "3级类目id", example = "1")
    @NotNull(message = "3级类目id不能为空",groups = {MingruiOperation.Add.class})
    private Integer cid3;
    @ApiModelProperty(value = "商品所属品牌id", example = "1")
    @NotNull(message = "商品所属品牌id不能为空",groups = {MingruiOperation.Add.class})
    private Integer brandId;
    @ApiModelProperty(value = "是否上架，0下架，1上架", example = "1")
    private Integer saleable;
    @ApiModelProperty(value = "是否有效，0已删除，1有效", example = "1")
    private Integer valid;
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;
}
