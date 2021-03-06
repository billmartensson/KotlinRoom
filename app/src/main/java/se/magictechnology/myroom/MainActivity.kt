package se.magictechnology.myroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var shopadapter : ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shopadapter = ShopAdapter(this)

        var shopRV = findViewById<RecyclerView>(R.id.shopRV)
        shopRV.layoutManager = LinearLayoutManager(this)
        shopRV.adapter = shopadapter

        shopadapter.loadShopping()

        findViewById<Button>(R.id.addBtn).setOnClickListener {
            val addtext = findViewById<EditText>(R.id.addET).text.toString()

            GlobalScope.launch(Dispatchers.IO) {

                findViewById<EditText>(R.id.amountET).text.toString().toIntOrNull()?.let {
                    val addShop = ShopDB.ShoppingItem(shopName = addtext, shopAmount = it)

                    shopadapter.shopdb.shopdb.ShoppingDao().save(addShop)

                    shopadapter.loadShopping()
                }


            }

        }





        // TESTAR HÄMTA SHOPPINGLISTOR
        /*
        val allTheLists = shopadapter.shopdb.shopdb.ShoppingDao().getListsWithItems()
        for(thelist in allTheLists)
        {
            val listname = thelist.shoplist.shopListName

            for(theitem in thelist.shopitems)
            {
                val itemname = theitem.shopName
            }
        }
        */




        var thedb = initDb()

        var testuser = User(firstName = "Bill", lastName = "Mårtensson")
        GlobalScope.launch(Dispatchers.IO) {
            thedb.userDao().save(testuser)
        }


        GlobalScope.launch(Dispatchers.IO) {


            for(usr in thedb.userDao().loadAll())
            {
                Log.i("roomdebug", usr.firstName!! + " " + usr.uid.toString())
            }

            //thedb.userDao().delete(thedb.userDao().loadAll().first())


        }


    }

    fun initDb(): AppDatabase {
        return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "user-db"
        ).build()
    }
}

@Entity
data class User(
        @PrimaryKey(autoGenerate = true) val uid: Int = 0,
        @ColumnInfo(name = "first_name") val firstName: String?,
        @ColumnInfo(name = "last_name") val lastName: String?
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun loadAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user WHERE uid = :delid")
    fun deleteWithId(delid : Int)

}

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}