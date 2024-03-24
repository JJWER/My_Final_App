package th.ac.kku.final17

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductViewModel: ViewModel() {

    private var db = Firebase.firestore
    private val products = "products"

    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val getListLiveData: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>()
    }

    val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun create(product: Product) {
        val docRef = db.collection(products)
        docRef.add(product.toMap()).addOnSuccessListener {
            createLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }


    fun update(product: Product) {
        val docRef = db.collection(products)
        docRef.document(product.id!!).update(product.toMap()).addOnSuccessListener {
            updateLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("update", it.localizedMessage!!)
            updateLiveData.postValue(false)
        }
    }

    fun delete(id: String) {
        val docRef = db.collection(products)
        docRef.document(id).delete().addOnSuccessListener {
            deleteLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("delete", it.localizedMessage!!)
            deleteLiveData.postValue(false)
        }
    }

    fun getListRealtime() {
        val docRef = db.collection(products)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e)
                return@addSnapshotListener
            }

            val products = ArrayList<Product>()
            for (item in snapshot!!.documents) {
                val product = Product()
                product.id = item.id
                product.name = item.data!!["name"] as String?
                product.price = item.data!!["price"] as Double?
                product.description = item.data!!["description"] as String?
                product.createDate = item.getTimestamp("createDate")
                product.updateDate = item.getTimestamp("updateDate")
                products.add(product)
            }

            getListLiveData.postValue(products)
        }
    }
}