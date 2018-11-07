mall-api

 #### 接口的请求值与响应值都是json(`下划线`)格式 , 通过 @JsonProperty
```
 接口请求规范
 添加请求 :POST 
 修改请求 :PUT  
 删除请求 :DELETE  
 查询请求 :GET(特殊情况用POST查询)
```


<pre>
.
├── README.md
├── base                // 基础包 
├── config              // 配置相关 
├── dao                 // dao层
├── domain              // 存放与数据库对应的实体
│   ├── constant        // 存放常量和枚举类
│   ├── common          // 通用的请求类
│   ├── request         // 请求封装类
│   ├── response        // 响应封装类
├── jobs                // 定时任务包
│   ├── impl
├── manager             // 缓存包
│   ├── impl
├── service             // service层
│   ├── impl            
├── util                // 存放工具类
├── web                 // 控制层

</pre>     