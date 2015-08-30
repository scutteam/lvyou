package com.scutteam.lvyou.interfaces;

import com.scutteam.lvyou.model.ViewSpot;

/**
 * Created by admin on 15/8/3.
 */
public interface ViewSpotListener {
    
    public void WhenViewSpotSelectIconClick(ViewSpot viewSpot);
    public void WhenViewSpotSelectIconClickSetFocus(ViewSpot viewSpot);//就是不管点击多少次都是表示选中
}
