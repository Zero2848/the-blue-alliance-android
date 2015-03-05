package com.thebluealliance.androidclient.test.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ActivityController;

/**
 * Created by phil on 3/5/15.
 */
@RunWith(RobolectricTestRunner.class)
public abstract class AbstractActivityTest {
    
    ActivityController controller;
    Class clRef;
    
    public AbstractActivityTest(Class cl){
        this.clRef = cl;
    }
    
    @Before
    public void constructActivity(){
        controller = Robolectric.buildActivity(clRef);
    }
    
    @Test
    public void testCreateActivity(){
        controller.create();
    }
    
    @Test
    public void testStartActivity(){
        controller.create().start();
    }
    
    @Test
    public void testResumeActivity(){
        controller.create().start().resume();
    }
    
}
