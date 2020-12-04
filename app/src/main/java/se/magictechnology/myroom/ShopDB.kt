package se.magictechnology.myroom

import android.content.Context
import androidx.room.*

class ShopDB(ctx : Context)
{
    lateinit var shopdb : ShoppingDatabase

    init {
        shopdb = Room.databaseBuilder(
            ctx,
            ShoppingDatabase::class.java, "shopping-db"
        ).build()
    }

    @Entity
    data class ShoppingItem(
        @PrimaryKey(autoGenerate = true) val uid: Int = 0,
        @ColumnInfo(name = "shop_name") val shopName: String?
    )

    @Dao
    interface ShoppingDao {
        @Query("SELECT * FROM shoppingitem")
        fun loadAll(): List<ShoppingItem>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun save(shoppingitem: ShoppingItem)

        @Delete
        fun delete(shoppingitem: ShoppingItem)

    }

    @Database(entities = arrayOf(ShoppingItem::class), version = 1)
    abstract class ShoppingDatabase : RoomDatabase() {
        abstract fun ShoppingDao(): ShoppingDao
    }
}