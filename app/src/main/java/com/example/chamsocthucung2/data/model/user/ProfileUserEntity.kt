package com.example.chamsocthucung2.data.model.user


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "profile_user")
data class ProfileUserEntity(
    // Thông tin người dùng cơ bản
    @PrimaryKey
    val uid: String, // Firebase UID
    val email: String = "", // Email đăng nhập
    val phoneNumber: String = "", // Số điện thoại
    val displayName: String = "", // Tên hiển thị
    val photoUrl: String? = null, // URL ảnh đại diện
    val isEmailVerified: Boolean = false, // Đã xác thực email
    val isAnonymous: Boolean = false, // Đăng nhập ẩn danh
    val isRegistered: Boolean = false, // Đã đăng ký
    val isLoggedIn: Boolean = false, // Đã đăng nhập
    val isBlocked: Boolean = false, // Đã khóa
    val isDeleted: Boolean = false, // Đã xóa
    val isSuspended: Boolean = false, // Đã bị khóa
    val isVerified: Boolean = false, // Đã xác thực
    val role: String = "", // Vai trò (admin, user, etc.)


    // Thông tin cá nhân
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "", // "male", "female", "other"
    val dateOfBirth: Long? = null, // Lưu dưới dạng timestamp
    val address: String = "",


    // Thông tin thú cưng
    val petName: String = "",
    val petType: String = "", // "dog", "cat", "bird", etc.
    val petBreed: String = "",
    val petGender: String = "",
    val petBirthDate: Long? = null,
    val petWeight: Float = 0f, // Cân nặng (kg)
    val petSpecialNotes: String = "", // Ghi chú đặc biệt


    // Thông tin hình ảnh
    val avatarUrl: String? = null, // URL ảnh đại diện
    val petImageUrl: String? = null, // URL ảnh thú cưng


    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long? = null
) {
    // Helper function để lấy tên đầy đủ
    fun getFullName(): String = "$firstName $lastName".trim()


    // Helper function để tính tuổi thú cưng
    fun getPetAge(): Int? {
        return petBirthDate?.let { birthDate ->
            val diff = System.currentTimeMillis() - birthDate
            (diff / (1000L * 60 * 60 * 24 * 365)).toInt()
        }
    }
}
