# 常用SQL记录
## DDL
#### 加列
```sql
ALTER TABLE `cho_bill_supplier_base_rule`
ADD COLUMN `rounding_num` decimal(4,2) NOT NULL DEFAULT 0.00 COMMENT '取整系数' ;
```

#### 更改列定义
```sql
ALTER TABLE `cho_bill_supplier_base_rule_detail` 
MODIFY COLUMN `first_part_price` decimal(18,4) NOT NULL DEFAULT '0.0000' COMMENT '首重费用'; 
```

#### 加索引
```sql
ALTER TABLE `cho_bill_carrier_cost` ADD INDEX `idx_composition` ( `vendor_order_no` );
```


## DML

#### 复制列属性
    (例如把id的值复制到freight_line_contract_id列上)
```sql
update frt_freight_line_contract set freight_line_contract_id=id;
```


## QUERY
###### 查询某个元素在列表形式的String是否存在？addressCodes：1,2,5,7
```sql
SELECT * FROM address WHERE FIND_IN_SET('5', addressCodes);
```
