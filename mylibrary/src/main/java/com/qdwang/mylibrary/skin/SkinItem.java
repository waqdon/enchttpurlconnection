package com.qdwang.mylibrary.skin;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * author: create by qdwang
 * date: 2018/11/15 11:45
 * described：
 */
public class SkinItem {

    public View view;
    public List<SkinAttr> skinAttrs;

    public SkinItem(View view, List<SkinAttr> skinAttrs) {
        this.view=view;
        this.skinAttrs=skinAttrs;
    }

    public void apply() {
        for (SkinAttr skinAttr: skinAttrs) {
            Log.e("qdwang=============", "skinAttr.attrName = " + skinAttr.attrName);
            if("background".equals(skinAttr.attrName)){
                if("color".equals(skinAttr.attrType)){
                    view.setBackgroundColor(SkinsManager.getInstance().getColor(skinAttr.id));
                }else if("drawable".equals(skinAttr.attrType)){
                    view.setBackgroundDrawable(SkinsManager.getInstance().getDrawable(skinAttr.id));
                }
            }else if("textColor".equals(skinAttr.attrName)){
                ((TextView)view).setTextColor(SkinsManager.getInstance().getColor(skinAttr.id));
            }else if("src".equals(skinAttr.attrName)){
                if("color".equals(skinAttr.attrType)){
                    ((ImageView)view).setImageResource(SkinsManager.getInstance().getColor(skinAttr.id));
                }else if("drawable".equals(skinAttr.attrType)){
                    ((ImageView)view).setImageDrawable(SkinsManager.getInstance().getDrawable(skinAttr.id));
                }
            }
        }
    }
}
