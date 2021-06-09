package com.istrong.wcedla.base.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.istrong.wcedla.base.database.entity.Article

@Dao
interface ArticleDao {

    @Insert
    suspend fun insertArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Update
    suspend fun updateArticle(article: Article)

    @Query("select * from article")
    fun queryAllArticle(): LiveData<List<Article>>


}