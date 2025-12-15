package com.testing.inventorytracking;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface Roominterface {


    @Insert
    void InsertAssets( List<Tiasset> tiasset);

    @Update
    void updateAsset(Tiasset tiasset);


    @Query("select * from Asset_Information")
    List<Tiasset> getAll () ;

    @Query("DELETE FROM Asset_Information")
    void deleteAssets();

}
