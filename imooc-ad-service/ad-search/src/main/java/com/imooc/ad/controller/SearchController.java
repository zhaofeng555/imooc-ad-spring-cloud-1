package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.IgnoreResponseAdvice;
import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.client.vo.AdPlanGetRequest;
import com.imooc.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class SearchController {
  private final RestTemplate restTemplate;

  public SearchController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @SuppressWarnings("all") // 屏蔽警告信息
  @IgnoreResponseAdvice // 不想使用统一的响应
  @PostMapping("ad/getAdPlansByRibben")
  public CommonResponse<List<AdPlan>> getAdPlanByRibbon(
    @RequestBody AdPlanGetRequest request
    ){
    log.info("ad-search: getAdPlansByRibbon -> {}",
      JSON.toJSONString(request));
    return restTemplate.postForEntity(
      "http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan",
      request,
      CommonResponse.class //提供的序列化的格式
    ).getBody();
  }
}
