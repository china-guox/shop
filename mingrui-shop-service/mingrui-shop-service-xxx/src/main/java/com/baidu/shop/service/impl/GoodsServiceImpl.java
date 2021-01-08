package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.dto.SpuDetailDTO;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    @Override
    public Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO) {
        if(ObjectUtil.isNotNull(spuDTO.getPage()) && ObjectUtil.isNotNull(spuDTO.getRows()))
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());

        if(!StringUtils.isEmpty(spuDTO.getOrder()) && !StringUtils.isEmpty(spuDTO.getSort())) PageHelper.orderBy(spuDTO.getOrderBy());
        Example example = new Example(SpuEntity.class);
        Example.Criteria criteria = example.createCriteria();

        if(ObjectUtil.isNotNull(spuDTO.getSaleable()) && spuDTO.getSaleable() < 2)
            criteria.andEqualTo("saleable",spuDTO.getSaleable());
        if(!StringUtils.isEmpty(spuDTO.getTitle()))
            criteria.andLike("title","%" + spuDTO.getTitle() + "%");

        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);

        List<SpuDTO> spuDTOList = spuEntities.stream().map(spuEntity -> {
            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);
            //通过分类id集合查询数据
            List<CategoryEntity> categoryEntities = categoryMapper.selectByIdList(Arrays.asList(spuEntity.getCid1(), spuEntity.getCid2(), spuEntity.getCid3()));

            String categoryName = categoryEntities.stream().map(categoryEntity -> categoryEntity.getName()).collect(Collectors.joining("/"));
            spuDTO1.setCategoryName(categoryName);

            BrandEntity brandEntity = brandMapper.selectByPrimaryKey(spuEntity.getBrandId());
            spuDTO1.setBrandName(brandEntity.getName());
            return spuDTO1;
        }).collect(Collectors.toList());

        PageInfo<SpuEntity> spuEntityPageInfo = new PageInfo<>(spuEntities);

        return this.setResult(HTTPStatus.OK,spuEntityPageInfo.getTotal() + "",spuDTOList);
    }

    @Transactional
    @Override
    public Result<JSONObject> save(SpuDTO spuDTO) {

        if (ObjectUtil.isNotNull(spuDTO)) {
            final Date date = new Date();
            //新增spu
            SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
            spuEntity.setSaleable(1);
            spuEntity.setValid(1);
            spuEntity.setCreateTime(date);
            spuEntity.setLastUpdateTime(date);
            spuMapper.insertSelective(spuEntity);

            //新增spuDetail
            SpuDetailDTO spuDetail = spuDTO.getSpuDetail();
            SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDetail, SpuDetailEntity.class);
            spuDetailEntity.setSpuId(spuEntity.getId());
            spuDetailMapper.insertSelective(spuDetailEntity);

            //新增sku
            this.saveAndEditGoods(spuDTO,spuEntity.getId(),date);

            return this.setResultSuccess();
        }
        return this.setResultError("数据错误");
    }

    private void saveAndEditGoods(SpuDTO spuDTO,Integer spuId,Date date){
        //新增sku
        spuDTO.getSkus().stream().forEach(skuDTO -> {
            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuId);
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);

            //新增Stock
            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);
        });
    }
    @Transactional
    @Override
    public Result<JSONObject> edit(SpuDTO spuDTO) {
        if(ObjectUtil.isNotNull(spuDTO)){

            final Date date = new Date();
            SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
            spuEntity.setLastUpdateTime(date);
            spuMapper.updateByPrimaryKeySelective(spuEntity);

            spuDetailMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class));

           this.deleteSkuAndStock(spuEntity.getId());

            this.saveAndEditGoods(spuDTO,spuEntity.getId(),date);

            return this.setResultSuccess();
        }
        return this.setResultError("数据错误");
    }

    @Override
    public Result<SpuDetailEntity> getDetailBySpuId(Integer spuId) {
        if(ObjectUtil.isNotNull(spuId)) return this.setResultSuccess(spuDetailMapper.selectByPrimaryKey(spuId));
        return this.setResultError("获取数据失败");
    }

    @Override
    public Result<List<SkuDTO>> getSkuBySpuId(Integer spuId) {
        if(ObjectUtil.isNotNull(spuId)) return this.setResultSuccess(skuMapper.getSkuAndStockBySpuId(spuId));
        return this.setResultError("获取数据失败");
    }

    private void deleteSkuAndStock(Integer spuId){
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
        List<Long> collect = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());

        skuMapper.deleteByIdList(collect);
        stockMapper.deleteByIdList(collect);
    }
    @Transactional
    @Override
    public Result<JSONObject> deleteGoodsBySpuId(Integer spuId) {

        if(ObjectUtil.isNotNull(spuId)){
            spuMapper.deleteByPrimaryKey(spuId);
            spuDetailMapper.deleteByPrimaryKey(spuId);

           this.deleteSkuAndStock(spuId);

            return this.setResultSuccess();
        }

        return this.setResultError("数据错误");
    }

    @Override
    public Result<JSONObject> upAndDownStatus(SpuDTO spuDTO) {
        if(ObjectUtil.isNotNull(spuDTO)){
            spuMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class));
            return this.setResultSuccess();
        }
        return this.setResultError("获取数据失败");
    }
}
