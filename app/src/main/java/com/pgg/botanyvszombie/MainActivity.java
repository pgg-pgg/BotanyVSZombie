package com.pgg.botanyvszombie;

import android.app.Activity;
import android.os.Bundle;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

public class MainActivity extends Activity {

    private CCDirector ccDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CCGLSurfaceView ccglSurfaceView=new CCGLSurfaceView(this);//获取SurfaceView
        setContentView(ccglSurfaceView);

        ccDirector = CCDirector.sharedDirector();
        ccDirector.attachInView(ccglSurfaceView);//开启绘制线程

        ccDirector.setDisplayFPS(true);//设置显示帧率
        ccDirector.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);//强制横屏
        ccDirector.setScreenSize(480, 320);//设置屏幕分辨率，屏幕适配




        CCScene ccScene=CCScene.node();//创建一个场景对象
        DemoLayer firstLayer=new DemoLayer();//创建一个图层对象

        ccScene.addChild(firstLayer);//给场景添加图层
        ccDirector.runWithScene(ccScene);//导演运行场景


    }

    @Override
    protected void onResume() {
        super.onResume();
        ccDirector.resume();
        SoundEngine.sharedEngine().resumeSound();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ccDirector.pause();
        SoundEngine.sharedEngine().pauseSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ccDirector.end();
    }
}
