<div style="text-align: center;"><h2>初识智能上海科技有限公司技术架构管理后台文档介绍</h2></div>

开发作者：武艳坤(yankunw)

联系邮箱：`395854256@qq.com`

**版本发布说明:**

| 标题                          | 发布时间       |
|-----------------------------|------------|
| [firstouch-framework-2.0发布] | 2023-02-08 |

**备注说明：**

> 1、在使用的framework-2.0的版本是单体Spring Boot架构.集成mybatis-plus、mybatis、druid、knife4j 等技术<br>
> 2、本系统采用多模块组合的方式组成系统，内置模块、用户管理、角色管理、岗位管理、组织架构管理、地区管理、系统缓存、业务字典等功能

**具体操作：**
<pre>
一. 如何配置一个新的管理后台？<br>
二. 如何登录？<br>
    步骤1：
    复制脚本到登录接口的AfterScript 选项中
    脚本：
    var code=ke.response.data.code;
    if(code==200){
        var token_type=ke.response.data.data.token_type;
        var access_token=ke.response.data.data.access_token;
        var tenant_id=ke.response.data.data.tenant_id;
        var authorization=ke.response.data.data.authorization;
        var userType=ke.response.data.data.userType;
        var refresh_token=ke.response.data.data.refresh_token;
        ke.global.setHeader("ft-auth",token_type + " " + access_token);
        ke.global.setHeader("tenant_id",tenant_id);
        ke.global.setHeader("userType",userType);
        ke.global.setHeader("authorization",authorization);
        ke.global.setHeader("refresh_token",refresh_token);
    }
    步骤2：
    输入登录信息如下：
    {
      "account": "admin",
      "password": "21232f297a57a5a743894a0e4a801fc3",
      "tenantId": "000000",
      "userType": "1",
      "grantType": "password"
    }
    其中
    account 为账户名称 
    password 为密码md5加密后的数据
    tenantId 租户id
    userType 用户类型 1 管理后台，需要其他的类型需要开发时自己扩展
    grantType 授权类型 选项（password,refresh_token,captcha）
    其中password为用户名、密码登录，captcha 为用户名密码+图片验证码登录
    refresh_token 为token续期，需要在head 里边加入参数 refresh_token = 现有的refresh_token
    captcha 验证码登录需要首先调用【获取验证码】接口 获得 key 和 image,其次在登录接口的head中添加
    captchaKey=key 和 captchaCode= image 图片中的验证码
    
    验证码接口需要添加两个head
    ke.global.setHeader("captchaKey","");
    ke.global.setHeader("captchaCode","");



三. 如何开发一个接口？<br>
四. 如何添加客户端？有别于管理后台，比如 app客户端接口<br>
五. 如何添加一个租户？<br>
六. 如何配置角色并授权？<br>
七. 如何为数据配置权限和授权？<br>
</pre>
