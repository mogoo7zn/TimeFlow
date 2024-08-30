package cn.edu.ustc.timeflow.util

import android.content.Context

class SharedPreferenceHelper {
    companion object {
        fun saveString(context: Context, key: String, value: String) {
            val editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getString(context: Context, key: String): String? {
            val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, null)
        }

        fun saveInt(context: Context, key: String, value: Int) {
            val editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun getInt(context: Context, key: String): Int {
            val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            return sharedPreferences.getInt(key, 0)
        }

        fun saveBoolean(context: Context, key: String, value: Boolean) {
            val editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun getBoolean(context: Context, key: String, b: Boolean): Boolean {
            val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(key, false)
        }
    }
}