package com.abloom.mery.data.firebase.user

import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.model.User
import com.abloom.mery.data.firebase.toLocalDate
import com.abloom.mery.data.firebase.toTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.LocalDate

data class UserDocument(
    @JvmField @PropertyName(KEY_USER_ID) val id: String = "",
    @JvmField @PropertyName(KEY_NAME) val name: String = "",
    @JvmField @PropertyName(KEY_INVITATION_CODE) val invitationCode: String = "",
    @JvmField @PropertyName(KEY_SEX) val sex: Boolean = true,
    @JvmField @PropertyName(KEY_MARRIAGE_DATE) val marriageDate: Timestamp = Timestamp(0, 0),
    @JvmField @PropertyName(KEY_FIANCE) val fianceId: String? = null
) {

    fun asExternal() = User(
        id = id,
        name = name,
        marriageDate = marriageDate.toLocalDate(),
        sex = if (sex) Sex.MALE else Sex.FEMALE,
        invitationCode = invitationCode,
        fianceId = fianceId
    )

    companion object {

        const val KEY_USER_ID = "user_id"
        const val KEY_NAME = "name"
        const val KEY_INVITATION_CODE = "invitation_code"
        const val KEY_SEX = "sex"
        const val KEY_MARRIAGE_DATE = "marriage_date"
        const val KEY_FIANCE = "fiance"

        fun create(
            id: String,
            name: String,
            marriageDate: LocalDate,
            sex: Sex,
            invitationCode: String
        ): UserDocument = UserDocument(
            id = id,
            name = name,
            marriageDate = marriageDate.toTimestamp(),
            sex = sex == Sex.MALE,
            invitationCode = invitationCode
        )
    }
}
