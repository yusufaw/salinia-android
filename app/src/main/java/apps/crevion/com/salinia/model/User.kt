package apps.crevion.com.salinia.model

import com.google.gson.annotations.SerializedName

/**
 * Created by yusufaw on 2/2/18.
 */

class User(
        val id: String,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String,
        val email: String,
        val photoProfile: String)
