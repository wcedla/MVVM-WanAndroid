package com.istrong.wcedla.base.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.istrong.wcedla.base.database.dao.ArticleDao
import com.istrong.wcedla.base.database.entity.Article

@Database(entities = [Article::class], version = 1)
abstract class WanAndroidDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {

        private var INSTANCE: WanAndroidDatabase? = null

        fun getDatabase(context: Context): WanAndroidDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WanAndroidDatabase::class.java,
                    "inspect_database"
                )//.fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }

}