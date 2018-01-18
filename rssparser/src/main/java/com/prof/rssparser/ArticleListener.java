package com.prof.rssparser;


import java.util.List;

interface ArticleListener {
    void onParsedArticle(Article article);
    void onParseComplete(List<Article> articles);
}
