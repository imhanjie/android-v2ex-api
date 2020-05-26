## V2EX API

一个 [V2EX 社区](https://v2ex.com/)（以下简称 V 站） API 库，基于 okhttp 拦截器实现。虽然 V 站提供了一些 [API 接口](https://v2ex.com/p/7v9TEc53)，不过接口数量少且无用户登录相关接口，所以该库的目的就是通过解析 V 站的 HTML 静态页面数据以生成相应的 Json 接口数据，来使你方便的编写自己的 V2EX 社区客户端，无需关心数据解析的过程。



### 集成

1. 添加 Gradle 依赖：

   ```groovy
   implementation 'com.imhanjie.library:v2ex-api:0.1.3'
   ```

2. 在 Application 中初始化：

   ``` kotlin
   class MyApp : MultiDexApplication() {
   
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

3. 配置 okhttp 客户端：

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

   - 要禁用 302 重定向。
   - `addInterceptor()` 添加的**拦截器顺序不能错误**。





### 使用

下面以访问 V2EX 首页数据接口为例：

> 我这里使用的是 Retrofit 请求框架，可自行使用你项目中请求方式。

``` kotlin
interface ApiService {

    @GET("/")
    fun loadLatestTopics(
        @Query("tab") tab: String
    ): String

}
```

返回的数据结构如下：

``` json
{
  "code": 1
}
```

