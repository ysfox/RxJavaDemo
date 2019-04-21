package site.qinyong.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                rxJavaNormUse();
                break;
            case R.id.button2:
                rxJavaEasyUse();
                break;
            case R.id.button3:
                rxJavaSendMultipleSingle();
                break;
            case R.id.button4:
                rxJavaSendSingleOtherWay();
                break;
        }
    }

    /**
     * rxJava常规使用方法
     */
    private void rxJavaNormUse() {
        //1.RxJava2的基本使用
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Hello");        //发送常规的信号
                emitter.onNext("World");        //发送常规的信号
                emitter.onError(new Throwable());      //发送错误的信号，接收到错误信号之后就不在发送剩余的信号了
                emitter.onComplete();                  //发送完成的信号
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.e("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                LogUtils.e("onNext，接收到的信号消息->" + s);
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("onError" + e);
            }

            @Override
            public void onComplete() {
                LogUtils.e("onComplete");
            }
        });
    }

    /**
     * RxJava的简写方式
     */
    private void rxJavaEasyUse() {
        //如果不关系错误和完成的信号则可以直接简写写如下
        /*
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Hello");                //发送常规的信号
                emitter.onNext("World");                //发送常规的信号
                emitter.onError(new Throwable());       //发送错误的信号
                emitter.onComplete();                   //发送完成的信号
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });*/
        //如果要关心普通信号，错误信号，完成信号则写法如下
        //Disposable d可以用来取消订阅以及判断是否在订阅
        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Hello");        //发送常规的信号
                emitter.onNext("Bitch");        //发送常规的信号
                emitter.onError(new Throwable());      //发送错误的信号，接收到错误信号之后就不在发送剩余的信号了
                emitter.onComplete();                  //发送完成的信号
            }
        }).subscribe(new Consumer<String>() {         //这个Consumer相当于上面的OnNext
            @Override
            public void accept(String s) throws Exception {
                LogUtils.e("accept->" + s);
            }
        }, new Consumer<Throwable>() {               //这个Consumer相当于上面的onError
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e("accept->" + throwable);
            }
        }, new Action() {                           //这个Action相当于上面的onComplete
            @Override
            public void run() throws Exception {
                LogUtils.e("Action");
            }
        });
    }

    /**
     * RxJava一次性发送多个信号的简写方式
     */
    private void rxJavaSendMultipleSingle() {
        //Disposable d可以用来取消订阅以及判断是否在订阅
        Disposable disposable =  Observable.just("吃饭","学习","睡觉","上班").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.e("accept->" + s);
            }
        });
    }

    /**
     * RxJava中发送信号的其他多重方式
     */
    private void rxJavaSendSingleOtherWay(){
        //发送信号的其他方式如下
        //Observable.just
        //Observable.fromArray
        //Observable.fromCallable
        //Observable.fromFuture  //老子不会
        //第一种方式
        Disposable disposable1 =  Observable.just("泡妞","蹦迪","撩妹子").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.e("accept->" + s);
            }
        });

        //第二种方式
        Disposable disposable2 =  Observable.fromArray("游戏","吃饭","睡觉").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.e("accept->" + s);
            }
        });

        //第三种方式，这种方式只能发送一次信号
        Disposable disposable3 = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "吃麻辣烫";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.e("accept->" + s);
            }
        });
    }
}
