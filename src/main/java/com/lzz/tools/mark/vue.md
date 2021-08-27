##### 按钮样式
- primary
- success
- info
- warning
- danger

### 使用技巧
##### 1.去除前后空格trim

```css
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


##### el-table type="expand" 子项无数据时不显示展开按钮
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

