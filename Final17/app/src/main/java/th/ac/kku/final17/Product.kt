package th.ac.kku.final17

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Product(
    var id: String? = null,
    var name: String? = null,
    var price: Double? = null,
    var description: String? = null,
    var createDate: Timestamp? = null,
    var updateDate: Timestamp? = null
)
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "description" to description,
            "createDate" to createDate,
            "updateDate" to updateDate,
        )
    }
}
