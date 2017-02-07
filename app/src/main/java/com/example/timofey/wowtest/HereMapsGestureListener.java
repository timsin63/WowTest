package com.example.timofey.wowtest;

import android.graphics.PointF;
import android.util.Log;

import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;

import java.util.List;

/**
 * Created by timofey on 07.02.2017.
 */

public class HereMapsGestureListener implements MapGesture.OnGestureListener {

    public HereMapsGestureListener(){

    }

    @Override
    public void onPanStart() {

    }

    @Override
    public void onPanEnd() {

    }

    @Override
    public void onMultiFingerManipulationStart() {

    }

    @Override
    public void onMultiFingerManipulationEnd() {

    }

    @Override
    public boolean onMapObjectsSelected(List<ViewObject> list) {

        for (ViewObject viewObject : list){

            if (viewObject.getClass().equals(MapMarker.class)){
                MapMarker marker = (MapMarker) viewObject;

                marker.showInfoBubble();

                return true;
            }
        }
        return true;
    }

    @Override
    public boolean onTapEvent(PointF pointF) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(PointF pointF) {
        return false;
    }

    @Override
    public void onPinchLocked() {

    }

    @Override
    public boolean onPinchZoomEvent(float v, PointF pointF) {
        return false;
    }

    @Override
    public void onRotateLocked() {

    }

    @Override
    public boolean onRotateEvent(float v) {
        return false;
    }

    @Override
    public boolean onTiltEvent(float v) {
        return false;
    }

    @Override
    public boolean onLongPressEvent(PointF pointF) {
        return false;
    }

    @Override
    public void onLongPressRelease() {

    }

    @Override
    public boolean onTwoFingerTapEvent(PointF pointF) {
        return false;
    }
}
