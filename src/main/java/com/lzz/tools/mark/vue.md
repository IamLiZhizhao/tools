##### 按钮样式
- primary
- success
- info
- warning
- danger

### 使用技巧
##### 1.去除前后空格trim

```js
/*使用trim修饰符*/
<input v-model.trim = "massage" >
```
    
##### 2.实现改变默认值字体颜色：

```css
.placeholder-color {
   /deep/ ::-webkit-input-placeholder {
       color: #000000;
    }
}
```


##### 3.el-table type="expand" 子项无数据时不显示展开按钮
###### html部分：
    <el-table
      :row-class-name="getRowClass"
    </el-table>

###### js部分：
    getRowClass(row,rowIndex){
        if(row.tableData.length === 0){  //判断当前行是否有子数据或者根据实际情况设置
              return 'row-expand-cover'
      }
    }
###### css部分：
    /deep/ .el-table .row-expand-cover .cell .el-table__expand-icon {
      display: none;
    }

#### 下拉框实现输入过滤
```js
<el-select v-model="carrierCode" clearable filterable placeholder="请选择">
              <el-option
                v-for="item in carrierCodeOptions"
                :key="item.carrierCode"
                :label="item.carrierName + '(' + item.carrierCode + ')'"
                :value="item.carrierCode">
                <span style="float: left">{{ item.carrierName }}</span>
                <span style="float: right">&nbsp;&nbsp;{{ item.carrierCode }}</span>
              </el-option>
            </el-select>
```
filterable：开启过滤功能，可以对key进行查询


#### 该方法是把javascript对象转换成json字符串
    JSON.stringify()
> 理解JSON.stringify()高级用法: https://www.cnblogs.com/tugenhua0707/p/9800453.html

