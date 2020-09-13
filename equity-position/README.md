## 持股管理需求分析

### 功能分析

Transactions表是股票交易流水，Position表是持股数量

三种操作：Insert、Update、Cancel

交易种类：Buy买入、Sell卖出

版本变动：其中Insert时版本是1，每Update/Cancel一次版本加1，Cancel后就不可再更改所以版本不再变动

持股数量：所有Insert 和 Update状态的Buy操作减去Sell操作，Cancel的交易不计入账。

### 实现步骤

1、新建Spring Boot项目

2、配置数据源H2实现（测试环境）DataConfig.java 配置实现

3、架构搭建po、dto、service、controller、dao、config等

4、编写controller功能，并后推service、dao实现

5、写单元测试

### postman测试

#### 1、Insert

![eqity-insert](.\image\eqity-insert.png)

#### 2、update

##### 正常

![eqity-update-normal](.\image\eqity-update-normal.png)

##### 超出已买股票数

![eqity-update-cancel](.\image\eqity-update-cancel.png)

##### Cancel的交易不能再修改

![eqity-update-cancel](.\image\eqity-update-cancel.png)

#### 3、Cancel

![eqity-cancel](.\image\eqity-cancel.png)

#### 4、position

![eqity-position](.\image\eqity-position.png)