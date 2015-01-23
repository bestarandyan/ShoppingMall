package com.manyi.mall.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manyi.mall.provider.contract.BankListContract;
import com.manyi.mall.provider.contract.CityContract;
import com.manyi.mall.provider.contract.EstateSubAreaContract;
import com.manyi.mall.provider.contract.LocalHistoryContract;
import com.manyi.mall.provider.contract.PictureContract;
import com.manyi.mall.provider.contract.SystemPropertiesContract;
import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.helper.TableBuilder;
import com.tjeannin.provigen.helper.TableUpdater;
import com.tjeannin.provigen.model.Constraint;
import com.tjeannin.provigen.model.Constraint.OnConflict;

public class ManyiProvider extends ProviGenProvider {
    public static final String DATABASE_NAME = "manyi_ex.db";
    public static final int DATABASE_VERSION = 7;

    // 数据库表
    @SuppressWarnings("rawtypes")
    private static Class[] mContracts = new Class[]{CityContract.class, EstateSubAreaContract.class, LocalHistoryContract.class,
            SystemPropertiesContract.class, PictureContract.class, BankListContract.class};
    // 表名
    private static String[] mTableNames = new String[]{CityContract.TABLE_NAME, EstateSubAreaContract.TABLE_NAME,
            LocalHistoryContract.TABLE_NAME, SystemPropertiesContract.TABLE_NAME, PictureContract.TABLE_NAME, BankListContract.TABLE_NAME};

    @SuppressWarnings("rawtypes")
    @Override
    public Class[] contractClasses() {
        return mContracts;
    }

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new SQLiteOpenHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase database) {
                System.out.println("database oncreate start !");
                new TableBuilder(CityContract.class).addConstraint(CityContract.CITY_LIST_CITY_ID, Constraint.UNIQUE, OnConflict.ABORT)
                        .createTable(database);
                new TableBuilder(EstateSubAreaContract.class).createTable(database);
                new TableBuilder(LocalHistoryContract.class).createTable(database);
                new TableBuilder(SystemPropertiesContract.class).createTable(database);
                new TableBuilder(PictureContract.class).createTable(database);
                new TableBuilder(BankListContract.class).createTable(database);
                System.out.println("database oncreate end !");
            }

            @Override
            public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
                System.out.println("database update start !");
                if (newVersion > oldVersion) {
                    int tempVersion = oldVersion;
                    switch (tempVersion) {
                        case 2:
                            System.out.println("database update start ! case: 2");
                            new TableBuilder(BankListContract.class).createTable(database);
                            tempVersion++;
                        case 3:
                            System.out.println("database update start ! case: 3");
                            String sql = "delete from " + LocalHistoryContract.TABLE_NAME;
                            database.execSQL(sql);
                            TableUpdater.addMissingColumns(database, LocalHistoryContract.class);
                            tempVersion++;
                        case 4:
                            System.out.println("database update start ! case: 4");
                            database.execSQL("drop table if exists " + CityContract.TABLE_NAME);
                            new TableBuilder(CityContract.class).addConstraint(CityContract.CITY_LIST_CITY_ID, Constraint.UNIQUE,
                                    OnConflict.ABORT).createTable(database);
                            tempVersion++;
                        case 5:
                            System.out.println("database update start ! case: 5");
                            String sqlString = "delete from " + LocalHistoryContract.TABLE_NAME;
                            database.execSQL(sqlString);
                            TableUpdater.addMissingColumns(database, LocalHistoryContract.class);
                            tempVersion++;
                        case 6:
                            System.out.println("database update start ! case: 6");
                            break;
                        default:
                            System.out.println("database update start ! case: default");
                            // 删除表
                            for (String tableName : mTableNames) {
                                database.execSQL("drop table if exists " + tableName);
                            }
                            System.out.println("Database tables is deleted!");
                            // 创建表
                            onCreate(database);
                            System.out.println("Database tables is recreated!");
                            break;
                    }

                }
                System.out.println("database update end !");
            }
        };
    }
}
