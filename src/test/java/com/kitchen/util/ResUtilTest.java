package com.kitchen.util;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ding
 */
public class ResUtilTest {
    @Test
    public void testReadAsStr() throws IOException {
        Assert.assertNull(ResUtil.readAsStr(null));
        Assert.assertNull(ResUtil.readAsStr("un-existed.json"));
        Assert.assertNotNull(ResUtil.readAsStr("kitchen_order.json"));
    }
}
