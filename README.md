# springbootDemo
使用springboot搭建前后端分离项目
主要根据老师的教程，熟悉了前后端分离的流程，后端也是分层架构，如下图所示
![text](https://github.com/vvshuai/JavaRepository/blob/master/%E5%9B%BE%E7%89%87/1.png)
## 1.数据库设计
本次系统主要是捋清下单流程，所以基于此我们设计了7个表：
* item表----------->商品实体表，包括商品名，价格，图片等
* item_stock表----->主要是记录商品库存
* order_info表----->订单表，基于时间生成id
* promo表---------->秒杀表，将商品变为秒杀单品，减少与正常商品的耦合性。
* sequenece_info表->
* user_info表------>用户信息表
* user_password表-->用户密码表，将密码进行拆分使用MD5加密(考虑重构)
## 2.异常设计
首先定义了一个接口，定制了错误码以及错误消息，其次定义了一个枚举类将常见的错误类型进行整合
