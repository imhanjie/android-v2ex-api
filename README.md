## V2EX API

一个 [V2EX 社区](https://v2ex.com/)（以下简称 V 站） API 库，基于 OkHttp 拦截器实现。虽然 V 站提供了一些 [API 接口](https://v2ex.com/p/7v9TEc53)，不过接口数量少且无用户登录相关接口，所以该库的目的就是通过解析 V 站的 HTML 静态页面数据以生成相应的 json 接口数据，来使你方便的编写自己的 V2EX 社区客户端，具有以下功能：

- 使用简单，无需关心繁琐的数据解析，直接拿到 json 数据。
- 内部自动管理页面相关 cookies。
- 覆盖绝大部分开发第三方客户端所需接口。
- 用户信息失效、以及其他错误处理。



## 集成

1. 添加 Gradle 依赖：

   ```groovy
   implementation 'com.imhanjie.library:v2ex-api:0.2.1'
   ```

2. 在 Application 中初始化：

   ``` kotlin
   class MyApp : Application() {
   
       override fun onCreate() {
           super.onCreate()
           ...
           V2exApi.init {
               // 返回在登录接口拿到的用户标识 cookie 值, 没有或未登录可返回 null or ""
               "return your cookie"
           }
           ...
       }
   
   }
   ```

3. 配置 OkHttp 客户端：

   ``` kotlin
   ...
   val builder = OkHttpClient.Builder()
   builder
       .followRedirects(false) // 禁用 302 重定向
       .followSslRedirects(false)	// 禁用 302 重定向
       .addInterceptor(HeaderInterceptor())
       .addInterceptor(LoginInterceptor())
       .addInterceptor(ParserInterceptor())
       .addInterceptor(CookieInterceptor)
   val okHttpClient: OkHttpClient = builder.build()
   ...
   ```

   ⚠️ 注意事项：

   - 需要禁用 302 重定向，因为框架内部单独处理了 302 情况。
   - `addInterceptor()` 添加的**拦截器顺序要按照 👆 的顺序，不可更改**。



## 示例

下面以访问 V2EX 首页数据接口为例，**更多完整接口文档见 👉 [项目 WiKi](https://github.com/imhanjie/android-v2ex-api/wiki)**。

> 我这里使用的是 Retrofit 请求框架，可自行使用你项目中请求方式，如果你也使用的是 Retrofit，可以参考我写的一个 V2EX 客户端项目的 [ApiService](https://github.com/imhanjie/android-v2ex-app/blob/master/app/src/main/java/com/imhanjie/v2ex/api/ApiService.kt) 完整接口文件。

``` kotlin
interface ApiService {

    @GET("/")
    fun loadLatestTopics(
        @Query("tab") tab: String
    ): RestfulResult<List<TopicItem>>

}
```

返回的数据结构如下：

``` json
{
  "code": 1,
  "data": [
    {
      "id": 650005,
      "isTop": true,
      "latestReplyTime": "36 分钟前",
      "nodeName": "promotions",
      "nodeTitle": "推广",
      "replies": 13,
      "title": "在线表格文档系统开发，五大技术难点及解决方案",
      "userAvatar": "https://cdn.v2ex.com/avatar/7e10/4ee6/465154_normal.png?m\u003d1579056398",
      "userName": "GrapeCityChina"
    },
    {
      "id": 675671,
      "isTop": false,
      "latestReplyTime": "刚刚",
      "nodeName": "games",
      "nodeTitle": "游戏",
      "replies": 1,
      "title": "有没有什么好玩的手机游戏推荐一下？安卓设备",
      "userAvatar": "https://cdn.v2ex.com/gravatar/9d1e4e86858d71d530436b6158c2bc79?s\u003d48\u0026d\u003dretro",
      "userName": "0x666666"
    }
  ]
}
```



## 数据流转与拦截器实现

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1gf68wdr1ntj31im0d8acj.jpg)

**项目中核心的 3 个拦截器简要说明：**

- **HeaderInterceptor**：用于处理部分接口请求时需要添加一些动态请求头。
- **LoginInterceptor**：专门用于处理登录接口是否登录成功以及登录失败原因的解析。
- **ParserInterceptor**：处理 302 重定向以及所有 html --> json 的数据解析。
- **CookieInterceptor**：用于缓存每个页面的 once 动态码与 cookies 的对应关系。





## 接口列表
**所有完整接口文档见 👉 [项目 WiKi](https://github.com/imhanjie/android-v2ex-api/wiki)**

##### 用户
- [x] [用户登录](https://github.com/imhanjie/android-v2ex-api/wiki/%E7%94%A8%E6%88%B7%E7%99%BB%E5%BD%95)
- [x] [当前登录用户的个人信息](https://github.com/imhanjie/android-v2ex-api/wiki/%E5%BD%93%E5%89%8D%E7%99%BB%E5%BD%95%E7%94%A8%E6%88%B7%E7%9A%84%E4%B8%AA%E4%BA%BA%E4%BF%A1%E6%81%AF)
- [x] [我的通知消息](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%88%91%E7%9A%84%E9%80%9A%E7%9F%A5%E6%B6%88%E6%81%AF)
- [x] [用户信息](https://github.com/imhanjie/android-v2ex-api/wiki/%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF)
- [ ] 用户所有主题
- [ ] 用户所有回复
- [x] [关注用户](https://github.com/imhanjie/android-v2ex-api/wiki/%E5%85%B3%E6%B3%A8%E7%94%A8%E6%88%B7)
- [x] [取消关注用户](https://github.com/imhanjie/android-v2ex-api/wiki/%E5%8F%96%E6%B6%88%E5%85%B3%E6%B3%A8%E7%94%A8%E6%88%B7)
- [x] [屏蔽用户](https://github.com/imhanjie/android-v2ex-api/wiki/%E5%B1%8F%E8%94%BD%E7%94%A8%E6%88%B7)
- [x] [取消屏蔽用户](https://github.com/imhanjie/android-v2ex-api/wiki/%E5%8F%96%E6%B6%88%E5%B1%8F%E8%94%BD%E7%94%A8%E6%88%B7)
##### 主题
- [x] [首页 Tab 主题](https://github.com/imhanjie/android-v2ex-api/wiki/%E9%A6%96%E9%A1%B5%20Tab%20%E4%B8%BB%E9%A2%98)
- [x] [更多新主题](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%9B%B4%E5%A4%9A%E6%96%B0%E4%B8%BB%E9%A2%98)
- [x] [指定节点下的主题](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%8C%87%E5%AE%9A%E8%8A%82%E7%82%B9%E4%B8%8B%E7%9A%84%E4%B8%BB%E9%A2%98)
- [x] [主题详情及回复](https://github.com/imhanjie/android-v2ex-api/wiki/%E4%B8%BB%E9%A2%98%E8%AF%A6%E6%83%85%E5%8F%8A%E5%9B%9E%E5%A4%8D)
- [x] [感谢回复](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%84%9F%E8%B0%A2%E5%9B%9E%E5%A4%8D)
- [x] [忽略主题](https://github.com/imhanjie/android-v2ex-api/wiki/%E5%BF%BD%E7%95%A5%E4%B8%BB%E9%A2%98)
- [x] [收藏主题](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%94%B6%E8%97%8F%E4%B8%BB%E9%A2%98)
- [x] [取消收藏主题](https://github.com/imhanjie/android-v2ex-api/wiki/%E5%8F%96%E6%B6%88%E6%94%B6%E8%97%8F%E4%B8%BB%E9%A2%98)
- [ ] 创建主题
- [ ] 主题添加附言
- [x] [我收藏的主题](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%88%91%E6%94%B6%E8%97%8F%E7%9A%84%E4%B8%BB%E9%A2%98)
##### 节点
- [x] [所有节点信息](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%89%80%E6%9C%89%E8%8A%82%E7%82%B9%E4%BF%A1%E6%81%AF)
- [x] [收藏节点](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%94%B6%E8%97%8F%E8%8A%82%E7%82%B9)
- [x] [取消收藏节点](https://github.com/imhanjie/android-v2ex-api/wiki/%E5%8F%96%E6%B6%88%E6%94%B6%E8%97%8F%E8%8A%82%E7%82%B9)
- [x] [我收藏的节点](https://github.com/imhanjie/android-v2ex-api/wiki/%E6%88%91%E6%94%B6%E8%97%8F%E7%9A%84%E8%8A%82%E7%82%B9)



## 感谢

- [V2EX](https://v2ex.com/)
- [OkHttp](https://square.github.io/okhttp/)
- [jsoup](https://github.com/jhy/jsoup)



## 最后

由于本项目的实现原理是通过解析 V2EX 网站的 HTML 数据来实现的，所以不可避免的遇到日后 V2EX 网站更新所可能导致的解析错误，所以本项目会持续更新，若有疑问或者额外的解析需求，例如想接口添加字段等，可以通过提 issue 来反馈。

