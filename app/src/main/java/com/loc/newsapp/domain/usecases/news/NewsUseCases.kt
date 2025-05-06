package com.loc.newsapp.domain.usecases.news


/*\
Data class será un envoltorio de todas las NewsCases de este paquete.
 */
data class NewsUseCases(
    val getNews: GetNews,
    val searchNews: SearchNews,
    val upsertArticle: UpsertArticle,
    val deleteArticle: DeleteArticle,
    val selectArticles: SelectArticles,
    val selectArticle: SelectArticle
)
