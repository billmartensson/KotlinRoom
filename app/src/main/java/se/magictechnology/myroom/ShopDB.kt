package se.magictechnology.myroom

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class ShopDB(ctx : Context)
{
    lateinit var shopdb : ShoppingDatabase

    init {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE shoppingitem ADD COLUMN shop_amount INTEGER")
            }
        }

        shopdb = Room.databaseBuilder(
            ctx,
            ShoppingDatabase::class.java, "shopping-db"
        ).addMigrations(MIGRATION_1_2).build()
    }

    /*
    @Entity
    data class ShoppingList(
            @PrimaryKey(autoGenerate = true) val shoplistid: Int = 0,
            @ColumnInfo(name = "shoplist_name") val shopListName: String?
    )
    */

    @Entity
    data class ShoppingItem(
        @PrimaryKey(autoGenerate = true) val uid: Int = 0,
        //@ColumnInfo(name = "itemlistid") val itemListId: Int?,
        @ColumnInfo(name = "shop_name") val shopName: String?,
        @ColumnInfo(name = "shop_amount") val shopAmount: Int?
    )

    /*
    data class ShoplistWithItems(
            @Embedded val shoplist: ShoppingList,
            @Relation(
                    parentColumn = "shoplistid",
                    entityColumn = "itemlistid"
            )
            val shopitems: List<ShoppingItem>
    )
    */

    @Dao
    interface ShoppingDao {
        /*
        @Query("SELECT * FROM shoppinglist")
        fun loadAllShoppingLists(): List<ShoppingList>

        @Query("SELECT * FROM shoppingitem WHERE itemlistid = listid")
        fun loadItemsInList(listid : Int): List<ShoppingItem>

        @Query("SELECT * FROM shoppinglist")
        fun getListsWithItems(): List<ShoplistWithItems>
        */

        @Query("SELECT * FROM shoppingitem")
        fun loadAll(): List<ShoppingItem>

        @Query("SELECT * FROM shoppingitem WHERE shop_amount = :number")
        fun loadAmount(number : Int): List<ShoppingItem>


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun save(shoppingitem: ShoppingItem)

        @Delete
        fun delete(shoppingitem: ShoppingItem)

        @Update
        fun updateShopItem(vararg shopitem: ShoppingItem)
    }

    @Database(entities = arrayOf(ShoppingItem::class), version = 2)
    abstract class ShoppingDatabase : RoomDatabase() {
        abstract fun ShoppingDao(): ShoppingDao
    }
}