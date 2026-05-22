package com.project.spendtrackrq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.spendtrackrq.data.local.entities.user.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Currently on single user can use it, so we just return 1
    @Query("SELECT * FROM UserTable LIMIT 1")
    fun observeUser(): Flow<UserEntity?>
    // suspend fun getUser(): UserEntity?


}