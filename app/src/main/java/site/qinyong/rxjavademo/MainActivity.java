package site.qinyong.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private TextView mTextView;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://qinyong.site:8090")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        initView();
    }

    private void initView() {
        mButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);
        mButton.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        Observable.create(new ObservableOnSubscribe<ResponseBean>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseBean> emitter) throws Exception {

                ResponseBean responseBean = api.getUserInfoWithPath().execute().body();

                emitter.onNext(responseBean);
            }
        }).subscribeOn(Schedulers.io())                     //subscribeOn()指定Observable（被观察者）的线程
                .observeOn(AndroidSchedulers.mainThread())  //observerOn()指定Observer（观察者）的线程
                .subscribe(new Observer<ResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBean responseBean) {
                        LogUtils.e("the response is " + responseBean);
                        mTextView.setText("user name is " + responseBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("the error is " + e);
                        mTextView.setText("user error is " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
