package com.alan.handler.gamelogic;

import com.alan.db.DBInsetTest;
import com.alan.db.DatabaseManager;
import com.google.inject.Singleton;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/25 17:36
 * @Description: TODO
 */
@Singleton
public class BattleService {


    public void doBattle(String msg) {

        DatabaseManager.getInstance().sengMsgToDatabase(new DBInsetTest(111,msg));

    }

}
