package com.pgg.botanyvszombie;

import android.view.MotionEvent;

import org.cocos2d.actions.base.CCFollow;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCTMXMapInfo;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.particlesystem.CCParticleSnow;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.util.CGPointUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PDD on 2017/8/25.
 */
public class DemoLayer extends CCLayer {

    private ArrayList<CGPoint> cgPoints;
    private final CCSprite sprite;
    private CCTMXTiledMap cctmxTiledMap;
    private CCParticleSystem system;

    public DemoLayer(){
        sprite = CCSprite.sprite("z_1_01.png");
        sprite.setFlipX(true);
        sprite.setScale(0.5f);
        sprite.setAnchorPoint(0.5f, 0);
        LoadMap();
        particleSystem();
        setIsTouchEnabled(true);
    }

    private void LoadMap() {
        cctmxTiledMap = CCTMXTiledMap.tiledMap("map.tmx");
        cctmxTiledMap.setAnchorPoint(0.5f, 0.5f);
        cctmxTiledMap.setPosition(ccp(cctmxTiledMap.getContentSize().width / 2, cctmxTiledMap.getContentSize().height / 2));
        CCTMXObjectGroup cctmxObjectGroup= cctmxTiledMap.objectGroupNamed("roads");
        cgPoints=new ArrayList<>();
        ArrayList<HashMap<String,String>> point=cctmxObjectGroup.objects;
        for (HashMap<String, String> list:point){
            Integer x=Integer.parseInt(list.get("x"));
            Integer y=Integer.parseInt(list.get("y"));

            cgPoints.add(ccp(x,y));
        }
        this.addChild(cctmxTiledMap);
        sprite.setPosition(cgPoints.get(0));
        cctmxTiledMap.addChild(sprite);

        //地图跟随移动
//        CCFollow follow=CCFollow.action(sprite);
//        cctmxTiledMap.runAction(follow);
        walk();
        moveNext();
    }

    private void particleSystem() {
        system = CCParticleSnow.node();
        system.setTexture(CCTextureCache.sharedTextureCache().addImage("snow.png"));
        this.addChild(system, 1);
    }

    private int index;//僵尸当前的位置
    private int speed=80;

    public void moveNext(){
        index++;
        if (index<cgPoints.size()){
            CCMoveTo moveBy=CCMoveTo.action(CGPointUtil.distance(cgPoints.get(index-1),cgPoints.get(index))/speed,cgPoints.get(index));
            CCSequence sequence=CCSequence.actions(moveBy, CCCallFunc.action(this,"moveNext"));
            sprite.runAction(sequence);
        }else {
            system.stopSystem();
            dance();
        }
    }

    private void dance() {
        // sprite.stopAllActions();
        sprite.setAnchorPoint(ccp(0.5f, 0.5f));
        CCJumpBy jump = CCJumpBy.action(2, ccp(-20, 10), 10, 3);
        CCRotateBy rotate = CCRotateBy.action(1, 360);

        CCSpawn spawn = CCSpawn.actions(jump, rotate);

        CCSequence sequence = CCSequence.actions(spawn, spawn.reverse());

        CCRepeatForever repeat = CCRepeatForever.action(sequence);
        sprite.runAction(repeat);

        SoundEngine engine = SoundEngine.sharedEngine();
        engine.playSound(CCDirector.theApp, R.raw.psy, true);
    }


    private void walk(){
        ArrayList<CCSpriteFrame> frames=new ArrayList<>();
        String format="z_1_%02d.png";
        for (int i=1;i<=7;i++){
            frames.add(CCSprite.sprite(String.format(format,i)).displayedFrame());
        }

        CCAnimation animation=CCAnimation.animation("walk",.2f,frames);
        CCAnimate animate=CCAnimate.action(animation);

        CCRepeatForever repeat=CCRepeatForever.action(animate);
        sprite.runAction(repeat);

    }

    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        cctmxTiledMap.touchMove(event,cctmxTiledMap);
        return super.ccTouchesMoved(event);
    }

    class callLayer extends CCLayer{

        private final CCSprite ccSprite;

        public callLayer(){
            ccSprite = CCSprite.sprite("heart.png");
            CGSize winSize=CCDirector.sharedDirector().winSize();
            ccSprite.setPosition(winSize.width / 2, winSize.height / 2);
            this.addChild(ccSprite);
        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            CGPoint convertTouchToNodeSpace = convertTouchToNodeSpace(event);

            if (CGRect.containsPoint(ccSprite.getBoundingBox(), convertTouchToNodeSpace)) {
                DemoLayer.this.onEnter();
                removeSelf();
            }
            return super.ccTouchesBegan(event);
        }
    }
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        this.onExit();
        this.getParent().addChild(new callLayer());
        return super.ccTouchesBegan(event);
    }
}
