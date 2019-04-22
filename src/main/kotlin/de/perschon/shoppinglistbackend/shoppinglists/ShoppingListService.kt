package de.perschon.shoppinglistbackend.shoppinglists

import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.pullByFilter

class ShoppingListService : KoinComponent {

    private val collection by lazy {
        inject<CoroutineDatabase>().value.getCollection<ShoppingList>()
    }

    suspend fun getAllShoppingLists(): List<ShoppingList> = collection.find().toList()

    suspend fun getShoppingList(id: String): ShoppingList? = collection.findOne(ShoppingList::id eq id)

    suspend fun create(list: ShoppingList): ShoppingList {
        return list.also {
            collection.insertOne(it)
        }
    }

    suspend fun delete(id: String) {
        collection.deleteOne(ShoppingList::id eq id)
    }

    suspend fun deleteProduct(prodId: String) {
        collection.updateMany(
            EMPTY_BSON,
            update = pullByFilter(
                ShoppingList::items,
                ListItem::productId eq prodId
            )
        )
    }

}
