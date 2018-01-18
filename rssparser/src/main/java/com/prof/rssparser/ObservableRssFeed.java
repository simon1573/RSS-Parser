package com.prof.rssparser;


import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ObservableRssFeed implements ArticleListener, ObservableOnSubscribe<Article> {
    private String URL = "";
    private ObservableEmitter<Article> emitter;

    public ObservableRssFeed(String url) {
        this.URL = url;
    }

    public Observable<Article> getArticles() {
        return Observable.create(this);
    }

    @Override
    public void onParsedArticle(Article article) {
        emitter.onNext(article);
    }

    @Override
    public void onParseComplete(List<Article> articles) {
        emitter.onComplete();
    }

    @Override
    public void subscribe(ObservableEmitter<Article> e) throws Exception {
        this.emitter = e;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                new XmlParser(this).parseXML(response.body().string());
            } else throw new IOException("Failed to fetch RSS feed");
        } catch (IOException | XmlPullParserException exception) {
            exception.printStackTrace();
            emitter.onError(exception);
        }
    }
}
