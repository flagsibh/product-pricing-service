package com.inditex.hiring.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Use this POJO for offer service end point responses.
 */
public class Offer implements Serializable {

  private Long offerId;

  private Integer brandId;

  private String startDate;

  private String endDate;

  private Long priceListId;

  private String productPartnumber;

  private Integer priority;

  private BigDecimal price;

  private String currencyIso;

  public Offer() {

  }

  public Offer(Long offerId, Integer brandId, String startDate, String endDate, Long priceListId, String productPartnumber,
      Integer priority, BigDecimal price, String currencyIso) {

    this.offerId = offerId;
    this.brandId = brandId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.priceListId = priceListId;
    this.productPartnumber = productPartnumber;
    this.priority = priority;
    this.price = price;
    this.currencyIso = currencyIso;
  }

  public Long getOfferId() {
    return offerId;
  }

  public void setOfferId(Long offerId) {
    this.offerId = offerId;
  }

  public Integer getBrandId() {
    return brandId;
  }

  public void setBrandId(Integer brandId) {
    this.brandId = brandId;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Long getPriceListId() {
    return priceListId;
  }

  public void setPriceListId(Long priceListId) {
    this.priceListId = priceListId;
  }

  public String getProductPartnumber() {
    return productPartnumber;
  }

  public void setProductPartnumber(String productPartnumber) {
    this.productPartnumber = productPartnumber;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getCurrencyIso() {
    return currencyIso;
  }

  public void setCurrencyIso(String currencyIso) {
    this.currencyIso = currencyIso;
  }

}