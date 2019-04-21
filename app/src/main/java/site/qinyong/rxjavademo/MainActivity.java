package site.qinyong.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }


    //FlatMap操作符对原始Observable发射的每一项数据应用到一个你指定的函数，然后返回一个本省就是的Observable数据
    @Override
    public void onClick(View v) {
        //这里模拟retrofit从网络获取一个User数据，然后将user传递给rxJava
        User user = new User();
        user.setUid(110);
        Observable.just(user)
                .flatMap(new Function<User, ObservableSource<UserInfo>>() {
                    @Override
                    public ObservableSource<UserInfo> apply(User user) throws Exception {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUid(user.getUid());
                        userInfo.setAddress("北京");
                        userInfo.setAge(110);
                        userInfo.setName("阿狸");
                        userInfo.setSex(1);
                        //这里讲userInfo包装成一个Observable，如果是在retrofit则需要使用adapter-rxjava适配器自动包装
                        return Observable.just(userInfo);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        LogUtils.e("" + userInfo);
                    }
                });
    }

}
