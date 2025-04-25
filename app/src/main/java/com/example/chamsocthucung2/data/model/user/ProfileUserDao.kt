package com.example.chamsocthucung2.data.model.user


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ProfileUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: ProfileUserEntity)


    @Query("SELECT * FROM PROFILE_USER WHERE uid = :userId")
    fun getUserById(userId: String): Flow<ProfileUserEntity?>


    @Query("SELECT * FROM profile_user WHERE email = :email")
    fun getUserByEmailFlow(email: String): Flow<ProfileUserEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: ProfileUserEntity)


    @Query("SELECT * FROM profile_user WHERE uid = :uid")
    suspend fun getUserByUid(uid: String): ProfileUserEntity?


    @Query("SELECT * FROM profile_user WHERE email = :email")
    suspend fun getUserByEmail(email: String): ProfileUserEntity?


    @Query("SELECT * FROM profile_user WHERE uid = :uid")
    fun observeUserByUid(uid: String): Flow<ProfileUserEntity?>


    @Update
    suspend fun updateUser(user: ProfileUserEntity)


    @Delete
    suspend fun deleteUser(user: ProfileUserEntity)


    @Query("DELETE FROM profile_user")
    suspend fun clearAll()
    //Setting
//    @Query("SELECT * FROM PROFILE_USER LIMIT 1")
//    fun getUserProfile(): Flow<ProfileUserEntity?>
}
