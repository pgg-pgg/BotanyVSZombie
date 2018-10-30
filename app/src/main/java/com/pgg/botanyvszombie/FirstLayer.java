package com.pgg.botanyvszombie;

import android.view.MotionEvent;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * 第一个图层
 * Created by PDD on 2017/8/24.
 */
public class FirstLayer extends CCLayer {
    private static final int TAG=1;

    public FirstLayer(){
        CCSprite ccSprite= new CCSprite("z_1_attack_01.png");
        ccSprite.setAnchorPoint(0, 0);//设置锚点

        ccSprite.setPosition(ccp(100, 0));
//        ccSprite.setFlipX(true);//水平翻转
//        ccSprite.setFlipY(true);//垂直翻转
//        ccSprite.setOpacity(255);//设置不透明度，0~255
//        ccSprite.setScale(2);//宽高变为原来的两倍；
//        this.addChild(ccSprite);
        this.addChild(ccSprite,0,TAG);
        setIsTouchEnabled(true);
    }

    /**
     * 点击监听事件
     * @param event
     * @return
     */
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {

        CGPoint cgPoint=convertTouchToNodeSpace(event);
        CCSprite ccSprite=(CCSprite)this.getChildByTag(TAG);//通过Tag找到僵尸对象
        //判断此时触摸的点是否在僵尸的矩形区域内
        if (CGRect.containsPoint(ccSprite.getBoundingBox(),cgPoint)){

        }
        return super.ccTouchesBegan(event);
    }
}
