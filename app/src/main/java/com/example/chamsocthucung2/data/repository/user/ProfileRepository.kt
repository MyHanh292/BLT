package com.example.chamsocthucung2.data.repository.user


import com.example.chamsocthucung2.data.model.user.ProfileUserDao
import com.example.chamsocthucung2.data.model.user.ProfileUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull


class ProfileRepository(
    private val userDao: ProfileUserDao
) {
    // Lưu thông tin người dùng (insert hoặc update)
    suspend fun saveUser(user: ProfileUserEntity) {
        validateUser(user)
        userDao.insertOrUpdate(user)
    }


    // Lấy thông tin người dùng bằng email
    suspend fun getUserByEmail(email: String): ProfileUserEntity? {
        if (email.isBlank()) return null
        return userDao.getUserByEmail(email)
    }


    // Xóa người dùng
    suspend fun deleteUser(user: ProfileUserEntity) {
        userDao.deleteUser(user)
    }


    // Xóa tất cả người dùng
    suspend fun clearAll() {
        userDao.clearAll()
    }


    // Lấy thông tin người dùng theo ID (Flow)
    fun getUserById(userId: String): Flow<ProfileUserEntity?> {
        return userDao.getUserById(userId)
    }


    // Thêm người dùng mới
    suspend fun insertUser(user: ProfileUserEntity) {
        validateUser(user)
        userDao.insertUser(user)
    }


    // Insert hoặc update người dùng
    suspend fun insertOrUpdate(user: ProfileUserEntity) {
        validateUser(user)
        userDao.insertOrUpdate(user)
    }


    // Lấy thông tin profile dưới dạng Flow
    fun getProfileFlow(email: String): Flow<ProfileUserEntity> {
        if (email.isBlank()) throw IllegalArgumentException("Email cannot be blank")
        return userDao.getUserByEmailFlow(email).filterNotNull()
    }


    // Validate thông tin người dùng
    private fun validateUser(user: ProfileUserEntity) {
        if (user.email.isBlank()) throw IllegalArgumentException("Email cannot be blank")
        if (user.petName.isBlank()) throw IllegalArgumentException("Pet name cannot be blank")
    }
}
