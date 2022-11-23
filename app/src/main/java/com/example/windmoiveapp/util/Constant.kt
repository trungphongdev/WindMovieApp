package com.example.windmoiveapp.util

const val BASE_URL_YOUTUBE = "https://www.googleapis.com/youtube/v3"
const val API_KEY_GG_HIEPDINH = "AIzaSyDYAJAWiUDmNvVqMsF4KqwCZGKFxEGRLzE"
const val ADS_UNIT = "ca-app-pub-3292279140391234/1127228964"
const val TIME_OUT = 30000L
const val PASSWORD_CHAR_STYLE_DOT: Char = '.'
const val PASSWORD_HIDE_7_CHAR: String = "0000000"
const val PATTERN_TIME: String = "hh:mm:ss dd/MM/yyyy"
const val INFO_USER = 0
const val EDIT_USER = 1
const val REMOVE_USER = 2
const val INFO_MOVIE = 0
const val ADD_MOVIE = 1
const val REMOVE_MOVIE = 2
const val EDIT_MOVIE = 3
const val ZERO_INDEX = 0

/*const val CAMERA_PERMISSION_CODE  = 1
const val STORAGE_PERMISSION_CODE  = 1
const val WRITE_EXTERNAL_STORAGE = 1
const val READ_EXTERNAL_STORAGE = 1
const val PERMISSION_ALL  = 1*/
const val PERMISSION_REQUEST_CODE = 1

object HttpStatusCode {
    const val SUCCESS = 200
    const val BAD_REQUEST = 400
    const val UNAUTHENTICATED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val INTERNAL_SERVER_ERROR = 500
}

object ErrorCode {
    const val NETWORK_NOT_AVAILABLE = -100
    const val SERVER_EXCEPTION = -101
    const val UNKNOWN_ERROR = -102
}
