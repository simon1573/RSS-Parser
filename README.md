# RSS Parser
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)

This is an Android library to parse a RSS Feed. You can retrive the following information about an article:
<ul>
<li> Title
<li> Author
<li> Description
<li> Content
<li> Main Image
<li> Link
<li> Publication Date
<li> Categories
</ul>

**Disclaimer**: This library has been built starting from RSS feed generated from a Wordpress Site. Of course it's compatible with RSS feed generated from other tipe of sites; [Here](https://www.androidauthority.com/feed/) you can find an example of RSS feed.

## How to
#### Import:
The library is uploaded in jCenter, so you can easily add the dependency:
```Gradle
repositories {
    maven {
        url "https://jitpack.io"
    }
}
dependencies {
    compile 'com.github.simon1573:RSS-Parser:1.0'
}
```
#### Use:
```Java

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import com.prof.rssparser.Article;
import com.prof.rssparser.ObservableRssFeed;

private final CompositeDisposable compositeDisposable = new CompositeDisposable();

private List<Article> list = new ArrayList<>();
//url of RSS feed
String urlString = "http://www.androidcentral.com/feed";
ObservableRssFeed observable = new ObservableRssFeed(urlString);
        observable.getArticles().
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Article article) {
                        list.add(article);
                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(MainActivity.this, "Unable to load data.",
                                        Toast.LENGTH_LONG).show();
                                Log.i(MainActivity.class.getName(), "Unable to load articles");
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        Log.i(MainActivity.class.getName(), "Done loading articles");
                    }

                });
```
## Sample app
I wrote a simple app that shows articles from Android Authority. If in the article's content there isn't a image, a placeholder will be load. 

<img src="https://github.com/simon1573/RSS-Parser/blob/master/Screen.png" width="30%" height="30%">
