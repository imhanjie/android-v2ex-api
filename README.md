<center><h1>V2EX API</h1></center>



一个 [V2EX 社区](https://v2ex.com/)（以下简称 V 站） API 库，基于 OkHttp 拦截器实现。虽然 V 站提供了一些 [API 接口](https://v2ex.com/p/7v9TEc53)，不过接口数量少且无用户登录相关接口，所以该库的目的就是通过解析 V 站的 HTML 静态页面数据以生成相应的 Json 接口数据，来使你方便的编写自己的 V2EX 社区客户端，无需关心数据解析的过程。



## 集成

1. 添加 Gradle 依赖：

   ```groovy
   implementation 'com.imhanjie.library:v2ex-api:0.1.3'
   ```

2. 在 Application 中初始化：

   ``` kotlin
   class MyApp : Application() {
   
       override fun onCreate() {
           super.onCreate()
           ...
           V2exApi.init {
               // 返回在登录接口你拿到的用户标识 a2Cookie 值, 没有或未登录可返回 null or ""
               "xxxxxxxxxxx"
           }
           ...
       }
   
   }
   ```

3. 配置 OkHttp 客户端：

   ``` kotlin
   val okHttpClient: OkHttpClient
       get() {
           val builder = OkHttpClient.Builder()
           builder
               .followRedirects(false) // 禁用 302 重定向
               .followSslRedirects(false)	// 禁用 302 重定向
               .addInterceptor(LoginInterceptor())
               .addInterceptor(ParserInterceptor())
               .addInterceptor(CookieInterceptor)
           return builder.build()
       }
   ```

   ⚠️ 注意事项：

   - 要禁用 302 重定向，因为框架内部单独处理了 302 情况。
   - `addInterceptor()` 添加的**拦截器顺序要按照 👆 的顺序，不能错误**。



## 示例

下面以访问 V2EX 首页数据接口为例，更多完整接口文档见 👉 [项目 WiKi](https://github.com/imhanjie/android-v2ex-api/)。

> 我这里使用的是 Retrofit 请求框架，可自行使用你项目中请求方式，如果你也使用的是 Retrofit，参考我的项目的 [ApiService](https://github.com/imhanjie/android-v2ex-app/blob/master/app/src/main/java/com/imhanjie/v2ex/api/ApiService.kt) 完整接口文件。

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



## 原理





## 感谢

- [V2EX](https://v2ex.com/)
- [OkHttp](https://square.github.io/okhttp/)
- [jsoup](https://github.com/jhy/jsoup)



## 最后



