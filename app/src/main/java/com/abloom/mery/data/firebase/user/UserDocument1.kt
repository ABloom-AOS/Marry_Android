package com.abloom.mery.data.firebase.user

import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.model.User
import com.abloom.mery.data.firebase.Document
import com.abloom.mery.data.firebase.toLocalDate
import com.abloom.mery.data.firebase.toTimestamp1
import com.google.errorprone.annotations.Keep
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Keep
@Serializable
class UserDocument1(
    val user_id: String = "",
    val name: String = "",
    val invitation_code: String = "",
    val sex: Boolean = true,
    val marriage_date: Timestamp = Timestamp.now(),
    val fiance: String? = null,
    val fcm_token: String? = null,
) : Document {

    constructor(
        id: String,
        name: String,
        marriageDate: LocalDate,
        sex: Sex,
        invitationCode: String
    ) : this(
        user_id = id,
        name = name,
        marriage_date = marriageDate.toTimestamp1(),
        sex = sex == Sex.MALE,
        invitation_code = invitationCode
    )

    fun asExternal() = User(
        id = user_id,
        name = name,
        marriageDate = marriage_date.toLocalDate(),
        sex = if (sex) Sex.MALE else Sex.FEMALE,
        invitationCode = invitation_code,
        fianceId = fiance,
    )
}
