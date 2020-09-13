## 发货管理需求分析

### 功能分析

由文档看有3个功能，货物拆分、货物合并、货物增减

### 实现步骤

1、新建Spring Boot项目

2、配置数据源H2实现（测试环境）DataConfig.java 配置实现

3、架构搭建po、dto、service、controller、dao、config等

4、编写controller功能，并后推service、dao实现

5、写单元测试

### postman测试

1、Split测试将id为1的货物拆分成3个20、30、50

![split-test](.\image\split-test.png)

2、将id为2和3的两个货物合并为一个

![merge-test](.\image\merge-test.png)

3、货物量变动增加100

![increment-test](.\image\increment-test.png)

4、货物变动减少100

![decrement-test](.\image\decrement-test.png)