package com.example.chamsocthucung2.viewmodel.user


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


// Dữ liệu lịch hẹn
data class Appointment(
    val date: String = "",
    val time: String = "",
    val petName: String= "",
    val note: String = "",
    val petImage: String? = null,
    val userId: String? = FirebaseAuth.getInstance().currentUser?.uid // Thêm userId
)
// du lieu thu cung
data class ProfileInfo(
    var petName: String? = "",
    var petImage: String? = "",
    var petType: String? = "",
    var petFeature: String? = "",
    var petWeight: String? = "",
    var petAge: String? = "",
    var ownerName:String?="",
var address :String?="",
var phone :String?="",
)


class PetViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val petsCollection = firestore.collection("pets") // Tên collection lưu hồ sơ thú cưng
    private val appointmentsCollection = firestore.collection("appointments") // Tên collection lưu lịch hẹn


    private val _profileInfo = MutableStateFlow(ProfileInfo())
    val profileInfo: StateFlow<ProfileInfo> = _profileInfo


    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments


    // Lưu thông tin thú cưng lên Firestore
    fun savePetInfoToFireStore(petProfile: ProfileInfo) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            userId?.let {
                try {
                    petsCollection.document(userId) // Sử dụng uid làm ID
                        .set(petProfile)
                        .await()
                    Log.d("Firestore", "Hồ sơ thú cưng đã được lưu lên Firestore với ID (user UID): $userId")
                } catch (e: Exception) {
                    Log.w("Firestore", "Lỗi khi lưu hồ sơ thú cưng lên Firestore", e)
                }
            } ?: run {
                Log.w("PetViewModel", "Không thể lưu hồ sơ thú cưng vì chưa đăng nhập.")
            }
        }
    }


    init {
        getPetInfo()
        getAppointments()
    }
    // Hàm lấy thông tin thú cưng từ Firestore
    fun getPetInfo() {
        Log.i("AAA", "GetInfo")
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            userId?.let {
                try {
                    val document = petsCollection.document(userId).get().await()
                    if (document.exists()) {
                        val profile = document.toObject(ProfileInfo::class.java)
                        profile?.let {
                            _profileInfo.value = it
                            Log.d("Firestore", "Dữ liệu hồ sơ thú cưng đã được lấy từ Firestore (user UID): ${document.data}")
                        }
                    } else {
                        Log.d("Firestore", "Không tìm thấy hồ sơ thú cưng với UID: $userId trên Firestore")
                        _profileInfo.value = ProfileInfo()
                    }
                } catch (e: Exception) {
                    Log.d("Firestore", "Lỗi khi lấy hồ sơ thú cưng từ Firestore", e)
                }
            } ?: run {
                Log.w("PetViewModel", "Không thể lấy hồ sơ thú cưng vì chưa đăng nhập.")
            }
        }
    }


    // Cập nhật thông tin ProfileInfo trực tiếp trong ViewModel (lưu tạm thời trên app)
    fun setProfileInfo(petInfo: ProfileInfo) {
        _profileInfo.value = petInfo
        Log.d("PetViewModel", "ProfileInfo updated in ViewModel (local): ${_profileInfo.value}")
    }


    // Lưu thông tin thú cưng ngay trên app (chỉ lưu trong bộ nhớ của ViewModel)
    fun savePetInfoLocally(
        name: String, type: String, feature: String, weight: String, age: String,
        owner: String, mail: String, sdt: String
    ) {
        val petProfile = ProfileInfo(name, type, feature, weight, age)
        _profileInfo.value = petProfile
        Log.d("PetViewModel", "Hồ sơ thú cưng đã được lưu cục bộ (trong ViewModel): $petProfile")
    }


    fun saveAppointment(date: String, time: String, note: String, petName: String) { // Thêm tham số petName
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            val newAppointment = Appointment(
                petName = petName, // Sử dụng petName được truyền vào
                date = date,
                time = time,
                note = note,
                userId = userId
            )
            viewModelScope.launch {
                Log.d("PetViewModel", "Bắt đầu lưu lịch hẹn lên Firestore: $newAppointment")
                Log.d("PetViewModel", "Thông tin lịch hẹn - Tên thú cưng: $petName, Ngày: $date, Giờ: $time") // Thêm log
                try {
                    appointmentsCollection.add(newAppointment).await()
                    _appointments.update { currentAppointments -> currentAppointments + newAppointment }
                    Log.d("Firestore", "Lịch hẹn đã được lưu lên Firestore cho user $userId và thú cưng $petName")
                } catch (e: Exception) {
                    Log.w("Firestore", "Lỗi khi lưu lịch hẹn lên Firestore", e)
                }
            }
        } ?: run {
            Log.w("PetViewModel", "Không thể lưu lịch hẹn vì chưa đăng nhập.")
        }
    }


    // Hàm để lấy lịch hẹn từ Firestore (bạn sẽ cần triển khai hàm này)
    fun getAppointments() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                try {
                    appointmentsCollection
                        .whereEqualTo("userId", userId)
                        .get()
                        .await()
                        .documents.mapNotNull { it.toObject(Appointment::class.java) }
                        .let { fetchedAppointments ->
                            _appointments.value = fetchedAppointments
                            Log.d("Firestore", "Đã lấy ${fetchedAppointments.size} lịch hẹn từ Firestore cho user $userId")
                            fetchedAppointments.forEach {
                                Log.d("Firestore", "Lịch hẹn - Tên thú cưng: ${it.petName}, Ngày: ${it.date}, Giờ: ${it.time}")
                            }
                        }
                } catch (e: Exception) {
                    Log.w("Firestore", "Lỗi khi lấy lịch hẹn từ Firestore", e)
                }
            }
        } ?: run {
            Log.w("PetViewModel", "Không thể lấy lịch hẹn vì chưa đăng nhập.")
        }
    }


    // Hàm để cập nhật danh sách lịch hẹn cục bộ (nếu cần)
    fun updateAppointmentsLocally(newAppointments: List<Appointment>) {
        _appointments.value = newAppointments
    }
}

