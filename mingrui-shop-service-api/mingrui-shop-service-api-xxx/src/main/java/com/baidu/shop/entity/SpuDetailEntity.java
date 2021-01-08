package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "tb_spu_detail")
@Data
public class SpuDetailEntity {

    @Id
    @ApiModelProperty(value = "spu主键",example = "1")
    @NotNull(message = "spu主键不能为空", groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Integer spuId;

    @ApiModelProperty(value = "商品描述信息")
    private String description;

    @ApiModelProperty(value = "通用规格参数数据")
    @NotNull(message = "通用规格参数数据不能为空", groups = {MingruiOperation.Add.class})
    private String genericSpec;

    @ApiModelProperty(value = "特有规格参数及可选值信息，json格式")
    @NotNull(message = "特有规格参数不能为空", groups = {MingruiOperation.Add.class})
    private String specialSpec;

    @ApiModelProperty(value = "包装清单")
    private String packingList;

    @ApiModelProperty(value = "售后服务")
    private String afterService;
}
