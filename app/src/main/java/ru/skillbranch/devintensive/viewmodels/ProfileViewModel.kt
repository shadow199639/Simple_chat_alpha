package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.utils.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository
import ru.skillbranch.devintensive.utils.Utils

class ProfileViewModel : ViewModel() {
    private val repository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()
    private val _repositoryError = MutableLiveData<Boolean>()
    private val _isRepoClear = MutableLiveData<Boolean>()

    init {
        Log.d("M_ProfileViewModel", "init view model")
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    override fun onCleared() {
        Log.d("M_ProfileViewModel", "view model cleared")
        super.onCleared()
    }

    fun getProfileData(): LiveData<Profile> = profileData

    fun saveProfileData(profile: Profile){
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun getTheme() : LiveData<Int> = appTheme

    fun switchTheme() {
        if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun getRepositoryError(): LiveData<Boolean> = _repositoryError

    fun getRepoClear(): LiveData<Boolean> = _isRepoClear

    fun onRepositoryChanged(repo:String) {
        _repositoryError.value = !Utils.validCheck(repo)
    }

    fun onRepoEditCompleted(isError: Boolean) {
        _isRepoClear.value = isError
    }


}