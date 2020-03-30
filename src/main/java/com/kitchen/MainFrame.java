package com.kitchen;

import com.kitchen.job.EventListener;
import com.kitchen.job.OrderUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame {
    private static final String SENDING = "      --------";
    private static boolean isSending = false;

    private static JButton orderBtn;
    private static JLabel orderLbl;
    public static EventListener eventListener;

    static {
        orderBtn = new JButton("开始发送");
        orderLbl = new JLabel(SENDING);

        eventListener = new EventListener() {
            @Override
            public void eventUpdated(String address) {
                synchronized (MainFrame.class) {
                    orderLbl.setText(address);
                }
            }
        };

        orderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (MainFrame.class) {
                    isSending = !isSending;
                    orderBtn.setText(isSending ? "停止发送" : "开始发送");

                    if (isSending) {
                        OrderUtil.start();
                    } else {
                        OrderUtil.stop();
                        orderLbl.setText(SENDING);
                    }
                }
            }
        });
    }

    public static JFrame showFrame() {
        // create frame
        final JFrame frame = new JFrame("中央厨房订单管理系统，发送测试订单");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Box chatBox = Box.createVerticalBox();
        chatBox.add(orderBtn);
        chatBox.add(orderLbl);

        // create panel
        JPanel panel = new JPanel();
        panel.add(Box.createVerticalStrut(150));
        panel.add(chatBox);

        // show panel
        frame.setContentPane(panel);
        frame.setVisible(true);

        // do work
        frame.getRootPane().setDefaultButton(orderBtn);
        orderBtn.doClick();

        return frame;
    }
}
