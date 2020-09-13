package com.example.shipment.managment.dto;

import com.example.shipment.managment.service.ResultStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 查询结果外壳
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ResultDto<T> {
  private Integer code;
  private String msg;
  private T datas;
  private Integer total;
  private String exception;
  
  public static <T> ResultDto<T> success(T data) {
    return new ResultDto<T>(ResultStatusEnum.SUCCESS, data, null);
  }

  public static <T> ResultDto<T> success(T data, Integer total){
    return new ResultDto<>(ResultStatusEnum.SUCCESS, data, null, total);
  }

  public static <T> ResultDto<T> success(T data, ResultStatusEnum status){
    return new ResultDto<>(status, data, null, null);
  }

  public static <T> ResultDto<T> success(){
    return new ResultDto<>(ResultStatusEnum.SUCCESS);
  }

  public static <T> ResultDto<T> fail(ResultStatusEnum status){
    return new ResultDto<>(status);
  }

  public static <T> ResultDto<T> fail(ResultStatusEnum status, Exception exception){
    String exStr = "";
    return new ResultDto<T>(status, null, exStr);
  }
  
  public static <T> ResultDto<T> fail(ResultStatusEnum status, String exception) {
    String exStr = "";
    return new ResultDto<T>(status, null, exStr);
  }

  public ResultDto(ResultStatusEnum resultStatusEnum, T datas, String exception) {
    super();
    this.code = resultStatusEnum.getCode();
    this.msg = resultStatusEnum.getMsg();
    this.datas = datas;
    this.exception = exception;
  }

  public ResultDto(ResultStatusEnum resultStatusEnum, T datas, String exception, Integer total) {
    super();
    this.code = resultStatusEnum.getCode();
    this.msg = resultStatusEnum.getMsg();
    this.datas = datas;
    this.exception = exception;
    this.total = total;
  }

  public ResultDto(ResultStatusEnum resultStatusEnum) {
    this.code = resultStatusEnum.getCode();
    this.msg = resultStatusEnum.getMsg();
  }

  public Integer getCode() {
    return code;
  }
  public void setCode(Integer code) {
    this.code = code;
  }
  public String getMsg() {
    return msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }
  public T getDatas() {
    return datas;
  }
  public void setDatas(T datas) {
    this.datas = datas;
  }
  public String getException() {
    return exception;
  }
  public void setException(String exception) {
    this.exception = exception;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }
}

