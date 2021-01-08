package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Table(name = "tb_stock")
@Data
public class StockEntity {

    @Id
    @ApiModelProperty(value = "sku主键", example = "1")
    @NotNull(message = "sku主键不能为空", groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private Long skuId;
    @ApiModelProperty(value = "可秒杀库存", example = "1")
    private Integer seckillStock;
    @ApiModelProperty(value = "秒杀总数量", example = "1")
    private Integer seckillTotal;
    @ApiModelProperty(value = "库存数量", example = "1")
    private Integer stock;
}
