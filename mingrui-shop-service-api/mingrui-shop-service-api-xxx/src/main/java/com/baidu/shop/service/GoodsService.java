package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "商品接口")
public interface GoodsService {

    @ApiOperation(value = "获取spu信息")
    @GetMapping(value = "goods/getSpuInfo")
    public Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO);


    @ApiOperation(value = "新增商品")
    @PostMapping(value = "goods/save")
    public Result<JSONObject> save(@Validated({MingruiOperation.Add.class})@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "修改商品")
    @PutMapping(value = "goods/save")
    public Result<JSONObject> edit(@Validated({MingruiOperation.Update.class})@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "通过spuId获取Detail信息")
    @GetMapping(value = "goods/getDetailBySpuId")
    public Result<SpuDetailEntity> getDetailBySpuId(Integer spuId);

    @ApiOperation(value = "通过spuId获取sku信息")
    @GetMapping(value = "goods/getSkuBySpuId")
    public Result<List<SkuDTO>> getSkuBySpuId(Integer spuId);

    @ApiOperation(value = "通过spuId删除商品")
    @DeleteMapping(value = "goods/deleteGoodsBySpuId")
    public Result<JSONObject> deleteGoodsBySpuId(Integer spuId);

    @ApiOperation(value = "通过spuId下架商品")
    @PutMapping(value = "goods/upAndDownStatus")
    public Result<JSONObject> upAndDownStatus(@Validated({MingruiOperation.Update.class})@RequestBody SpuDTO spuDTO);
}
