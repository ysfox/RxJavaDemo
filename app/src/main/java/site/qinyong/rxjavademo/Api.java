package site.qinyong.rxjavademo;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2019/4/21.
 */
public interface Api {
    @GET("atguigu/json/HOME_URL.json")
    Call<ResponseBean> getUserInfoWithPath();
}
