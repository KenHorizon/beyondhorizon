package com.kenhorizon.libs.server;

import java.util.ArrayList;
import java.util.List;

public class ReloadableHandler {
    private static List<IReloadable> reloadList = new ArrayList<IReloadable>(768);

    public static void addToReloadList(IReloadable item) {
        reloadList.add(item);
    }

    public static List<IReloadable> getReloadList() {
        return reloadList;
    }
}
