package com.example.data.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtils {
    companion object{
        public fun startPermissionRequest(activity: Activity,permission : String,onComplete : PermissionResultListener){
            if(ContextCompat.checkSelfPermission(activity.applicationContext,permission)==PackageManager.PERMISSION_GRANTED){
                onComplete.onPermissionResult(true,false,permission)
            }else{
                onComplete.onPermissionResult(false,ActivityCompat.shouldShowRequestPermissionRationale(activity,permission),permission)
            }
        }
    }

    public interface PermissionResultListener{
        fun onPermissionResult(isItAllowed : Boolean,isShouldShowRequestPermission : Boolean,permission : String)
    }
}