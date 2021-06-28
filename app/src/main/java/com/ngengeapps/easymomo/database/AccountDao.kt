package com.ngengeapps.easymomo.database

import androidx.room.*
import com.ngengeapps.easymomo.models.Account

@Dao
interface AccountDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account:Account)


    @Query("SELECT * FROM account ORDER BY id")
    fun getAccounts():List<Account>



    //TODO: Add delete query
    @Delete
    suspend fun delete(account: Account)

    @Query("DELETE FROM account WHERE id = :id")
    suspend fun deleteById(id: Int)

    //TODO: Add clear query
    @Query("DELETE FROM account")
    suspend fun clear()

}