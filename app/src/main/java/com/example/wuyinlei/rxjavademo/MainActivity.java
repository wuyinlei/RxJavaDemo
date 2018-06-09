package com.example.wuyinlei.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        if (!e.isDisposed()) {
                            e.onNext("test");
                            e.onComplete();
                        }
                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        d.dispose();
                        Log.d("MainActivity", "onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.d("MainActivity", value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("MainActivity", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


//                //对于上面的是不存在被压的
                Flowable.create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(FlowableEmitter<String> e) {
                        if (!e.isCancelled()) {
                            e.onNext("test");
                            e.onComplete();
                        }
                    }
                }, BackpressureStrategy.DROP)
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                //响应式拉取策略
                                s.request(Long.MAX_VALUE);
                                Log.d("MainActivity", "Flowable onSubscribe");
                            }

                            @Override
                            public void onNext(String s) {
                                Log.d("MainActivity", "onComplete()" + s);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d("MainActivity", "Flowable onComplete()");
                            }
                        });
            }
        });
    }
}
