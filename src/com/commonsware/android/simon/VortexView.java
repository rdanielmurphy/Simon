package com.commonsware.android.simon;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
 
public class VortexView extends GLSurfaceView {
    private static final String LOG_TAG = VortexView.class.getSimpleName();
    private VortexRenderer _renderer;
    public VortexView(Context context) {
        super(context);
        _renderer = new VortexRenderer();
        setRenderer(_renderer);
    }
    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable() {
            public void run() {
            	_renderer.ScreenClick(event.getX(), event.getY());
            }
        });
        return true;
    }
}
