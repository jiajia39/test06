<div style="text-align: center;"><h2>初识智能上海科技有限公司技术架构运维中心文档介绍</h2></div>

开发作者：武艳坤(yankunw)

联系邮箱：`395854256@qq.com`

**版本发布说明:**

| 标题                          | 发布时间       |
|-----------------------------|------------|
| [framework-2.0发布] | 2023-02-08 |

**备注说明：**

> 1、在使用的framework-2.0的版本是单体Spring Boot架构.集成mybatis-plus、mybatis、druid、knife4j 等技术<br>
> 2、本系统采用多模块组合的方式组成系统，内置模块、用户管理、角色管理、岗位管理、组织架构管理、地区管理、系统缓存、业务字典等功能

**具体操作：**
<pre>
如何登录？<br>
    步骤1：
    复制脚本到登录接口的AfterScript 选项中
    脚本：
    var code=ke.response.data.code;
    if(code==200){
        var token_type=ke.response.data.data.token_type;
        var access_token=ke.response.data.data.access_token;
        var tenant_id=ke.response.data.data.tenant_id;
        var authorization=ke.response.data.data.authorization;
        ke.global.setHeader("ft-auth",token_type + " " + access_token);
        ke.global.setHeader("tenant_id",tenant_id);
        ke.global.setHeader("authorization",authorization);
    }
    步骤2：
    输入登录信息如下：
    {
      "account": "admin",
      "password": "21232f297a57a5a743894a0e4a801fc3"
    }
    其中
    account 为账户名称 
    password 为密码md5加密后的数据
</pre>
