package com.dheeraj.rxjavakt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import kotlin.text.StringBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var stringBuilder: StringBuilder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stringBuilder = StringBuilder()
        setContentView(R.layout.activity_main)
        getObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    private fun getObservable(): Observable<String> {
        val list =
            arrayListOf("Are", "You", "Ready", "For", "RxJava?", "In", "3....", "2....", "1....",
                "0....")
        return Observable.range(0, list.size).concatMap { value ->
            Observable.just(list[value]).delay(1, TimeUnit.SECONDS)
        }.doOnNext { it }
    }

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onComplete() {
                Log.d("In On complete:", "completed")
                startActivity(Intent(this@MainActivity, RxJavaActivity::class.java))
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("In onSubscribe:", "subscribed")
            }

            override fun onNext(t: String) {
                stringBuilder.append(t+"\n")
                tv_main.text = stringBuilder.toString()
            }

            override fun onError(e: Throwable) {
                Log.d("In on Error:", e.message ?: "No error message")
            }
        }
    }
}
