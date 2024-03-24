package th.ac.kku.final17

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import th.ac.kku.final17.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var name: EditText
    private lateinit var price: EditText
    private lateinit var description: EditText
    private lateinit var submit: Button
    private lateinit var rvList: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var list: ArrayList<Product>
    private var selected: Product = Product()
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        initElement()
        rvList = findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)
        list = ArrayList()
        productAdapter = ProductAdapter(list, this)
        rvList.adapter = productAdapter
        initViewModel()
        productViewModel.getListRealtime()
    }

    private fun initElement() {
        name = findViewById(R.id.name)
        price = findViewById(R.id.price)
        description = findViewById(R.id.description)
        submit = findViewById(R.id.submit)
        rvList = findViewById(R.id.rvList)

        // Add logout button listener
        binding.logoutButton.setOnClickListener {
            logout()
        }

        submit.setOnClickListener {
            create()
        }
        productViewModel.getListRealtime()
    }

    // Function to handle logout
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }

    private fun initViewModel() {
        productViewModel.createLiveData.observe(this) {
            onCreate(it)
        }
        productViewModel.updateLiveData.observe(this) {
            onUpdate(it)
        }
        productViewModel.deleteLiveData.observe(this) {
            onDelete(it)
        }
        productViewModel.getListLiveData.observe(this) {
            onGetList(it)
        }
    }

    private fun onCreate(it: Boolean) {
        if (it) {
            productViewModel.getListRealtime()
            resetText()
        }
    }

    private fun onUpdate(it: Boolean) {
        if (it) {
            productViewModel.getListRealtime()
            resetText()
            productAdapter.notifyDataSetChanged()
        }
    }

    private fun onDelete(it: Boolean) {
        if (it) {
            productViewModel.getListRealtime()
        }
    }

    private fun onGetList(it: List<Product>) {
        val diffCallback = ProductDiffCallback(list, it)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(it)
        diffResult.dispatchUpdatesTo(productAdapter)
    }

    private fun create() {
        val product = Product(
            selected.id,
            name.text.toString(),
            price.text.toString().toDouble(),
            description.text.toString(),
            Timestamp.now(),
            null
        )
        if (product.id != null) {
            productViewModel.update(product)
        } else {
            productViewModel.create(product)
        }
    }

    private fun resetText() {
        name.text = null
        price.text = null
        description.text = null
    }

    override fun onClick(item: Product, position: Int) {
        selected = item
        selected.updateDate = Timestamp.now()
        name.setText(selected.name)
        price.setText(selected.price.toString())
        description.setText(selected.description)
    }

    override fun onDelete(item: Product, position: Int) {
        productViewModel.delete(item.id!!)
    }

}