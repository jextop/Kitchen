package com.kitchen.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kitchen.MainFrame;
import com.kitchen.http.HttpUtil;
import com.kitchen.http.LocationUtil;
import com.kitchen.http.RespJsonObj;
import com.kitchen.util.CodeUtil;
import com.kitchen.util.JsonUtil;
import com.kitchen.util.LogUtil;
import com.kitchen.util.PoissonUtil;
import com.kitchen.util.ResUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;

public class OrderJob implements Job {
    private static final double LAMBDA = 3.25;
    private static int index = 0;
    private static JSONArray orderList;

    static {
        try {
            String jsonStr = ResUtil.readAsStr("data.json");
            orderList = JSONArray.parseArray(jsonStr);
        } catch (IOException e) {
            LogUtil.error(e.getMessage());
        }
    }

    @Override
    public synchronized void execute(JobExecutionContext var1) throws JobExecutionException {
        // Send random orders
        int count = orderList.size();
        int batch = PoissonUtil.getVariable(LAMBDA);
        for (int i = 0; i < batch; i++) {
            index = (index + i) % count;
            JSONObject order = orderList.getJSONObject(index);

            // Send message
            order.put("id", CodeUtil.getCode());
            sendOrder(order);
        }
    }

    private void sendOrder(JSONObject order) {
        LogUtil.info("Send order", JsonUtil.toStr(order));

        // 发送信息
        JSONObject ret = HttpUtil.sendHttpPost(
                "http://localhost:8011/kitchen/",
                null, order, new RespJsonObj()
        );
        System.out.println(JsonUtil.toStr(ret));

        // 更新界面
        String address = null;
        if (ret != null) {
            address = ret.getString("id");
        }
        MainFrame.eventListener.eventUpdated(address);
    }
}
