package com.commonsware.android.simon;

import java.util.LinkedList;
import java.util.Queue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SimonView extends SurfaceView implements SurfaceHolder.Callback {
	private GameEngine GE;
	private boolean bGameOver = false;
	private boolean bUserDone = false;
	
	public int iRound = 1;
	
	class GameEngineThread extends Thread {		    
	    Context ct;
	    Handler hd;
	    
        public GameEngineThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
        	    	GE =  new GameEngine();
        	    	ct = context;
        	    	hd = handler;
        }
        
        CharSequence[] cs = new CharSequence[2];
        
        @Override
        public void run() {
        	boolean bExitGame = false;
        	iRound = 1;
        	Queue<String> q = new LinkedList<String>();
            while (!bExitGame) {
            	while(!bGameOver){
	            	q = GE.Round(iRound);
	            	threadgui.RunClicks(q);
	            	//wait to get users queue
	            	while(!bUserDone && !bGameOver){}
	             	
	            	try {
	            		if(!bGameOver){
	            			sleep(1000);
							GE.ClearUserQueue();
							iRound++;
	            		}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}       
					bUserDone = false;
            	} 
            	MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.gameover);
       		 	mp.start(); 
            	bExitGame= true;
            }
        }
        
        private void EndGame(){
        	bGameOver = true;
        }
	}
    class GameGUIThread extends Thread {
        
        /*
         * State-tracking constants
         */
        public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_READY = 3;
        public static final int STATE_RUNNING = 4;
        public static final int STATE_WIN = 5;
        
        private float x;
        private float y;
        
        private int mCanvasWidth;
        private int mCanvasHeight;

        private long mLastTime;
        private Bitmap mSnowflake;
        
         /** Message handler used by thread to post stuff back to the GameView */
        private Handler mHandler;

         /** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
        private int mMode;
        
        /** Indicate whether the surface has been created & is ready to draw */
        private boolean mRun = false;
        
        /** Handle to the surface manager object we interact with */
        private SurfaceHolder mSurfaceHolder;

        public GameGUIThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;            
                    
            x = 0;
            y = 150;
            
        	mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simon1);
        }

        /**
         * Starts the game, setting parameters for the current difficulty.
         **/
        public void doStart() {
            synchronized (mSurfaceHolder) {
            	// Initialize game here!
            	
                x = 0;
                y = 150;
            	
                mLastTime = System.currentTimeMillis() + 100;
                setState(STATE_RUNNING);
            }
        }

        /**
         * Pauses the physics update & animation.
         */
        public void pause() {
            synchronized (mSurfaceHolder) {
              if (mMode == STATE_RUNNING) 
               	setState(STATE_PAUSE);
            }
        }
        
        @Override
        public void run() {       	
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        if (mMode == STATE_RUNNING) 
                        	updateGame();
                        doDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
            mSurfaceHolder = null;
        }

        /**
         * Used to signal the thread whether it should be running or not.
         * Passing true allows the thread to run; passing false will shut it
         * down if it's already running. Calling start() after this was most
         * recently called with false will result in an immediate shutdown.
         * 
         * @param b true to run, false to shut down
         */
        public void setRunning(boolean b) {
            mRun = b;
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @see #setState(int, CharSequence)
         * @param mode one of the STATE_* constants
         */
        public void setState(int mode) {
            synchronized (mSurfaceHolder) {
                setState(mode, null);
            }
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @param mode one of the STATE_* constants
         * @param message string to add to screen or null
         */
        public void setState(int mode, CharSequence message) {
            synchronized (mSurfaceHolder) {
                mMode = mode;
            }
        }

        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
            }
        }

        /**
         * Resumes from a pause.
         */
        public void unpause() {
            // Move the real time clock up to now
            synchronized (mSurfaceHolder) {
                mLastTime = System.currentTimeMillis() + 100;
            }
            setState(STATE_RUNNING);
        }
        
        public boolean isPaused(){
        	return mMode == STATE_PAUSE;
        }

        /**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        private void doDraw(Canvas canvas) {
        	// empty canvas
        	canvas.drawARGB(255, 0, 0, 0);
        	Paint p = new Paint();
	    	p.setColor(Color.WHITE);
	    	p.setTextSize(75);
	    	
	    	String sRound = Integer.toString(iRound - 1);        		

        	canvas.drawText(sRound, 135, 100 , p);
        	canvas.drawBitmap(mSnowflake, x, y, new Paint());
        }

        /**
         * Updates the game.
         */
        private void updateGame() {
        	//// <DoNotRemove>
            long now = System.currentTimeMillis();
            // Do nothing if mLastTime is in the future.
            // This allows the game-start to delay the start of the physics
            // by 100ms or whatever.
            if (mLastTime > now) 
            	return;
            double elapsed = (now - mLastTime) / 1000.0;
            mLastTime = now;
        }
    	
        public int colorDetector(int xPixel, int yPixel){
        	int color22=0;
    	     
    		 try{
    			 color22 = mSnowflake.getPixel(xPixel - (int)x, yPixel -(int)y);
    			 
    			 if (color22 == Color.YELLOW){
    				 PushYellow(true);  	
    			 }
    			 else if(color22 == -16767233){//blue
    				 PushBlue(true);  			 
    			 }
    			 else if(color22 == -14247168){//green
    				 PushGreen(true);  	 			 
    			 }
    			 else if(color22 == -65536){//red
    				 PushRed(true);  			 
    			 }    			 
    		 }catch(Exception e){
    			
    		 }        
        return color22;
        }
    
    private void PushRed(boolean uPush){
		 mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simonredpressed);
		 MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.red);
		 mp.start(); 
		 mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simon1);
		 if(uPush){
		 GE.AddClickToQueue(GE.RED);
		 CheckUserInput();
		 }
    }
    private void PushYellow(boolean uPush){
		 mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simonyellowpressed);
		 MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.yellow);
		 mp.start();   
		 mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simon1);
		 if(uPush){
		 GE.AddClickToQueue(GE.YELLOW);
		 CheckUserInput();
		 }
    }
    private void PushGreen(boolean uPush){
		 mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simongreenpressed);
		 MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.green);
		 mp.start(); 
		 mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simon1);
		 if(uPush){
		 GE.AddClickToQueue(GE.GREEN);
		 CheckUserInput();
		 }
    }
    private void PushBlue(boolean uPush){
		 mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simonbluepressed);
		 MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.blue);
		 mp.start(); 
		 mSnowflake = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.simon1);
		 if(uPush){
			GE.AddClickToQueue(GE.BLUE);
		 	CheckUserInput();
		 }
    }
    
    public void RunClicks(Queue<String> q){
    	int qSize = q.size();
    	for (int i = 0; i< qSize; i++){
    		String sColor = q.remove();
    		if(sColor.charAt(0) == '0')
    			PushRed(false);
    		else if(sColor.charAt(0) == '1')
    			PushYellow(false);
    		else if(sColor.charAt(0) == '2')
    			PushGreen(false);
    		else if(sColor.charAt(0) == '3')
    			PushBlue(false);
    		
    		q.add(sColor);
    		
    		try {
				sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    private void CheckUserInput(){
    	bGameOver = !GE.IsItAMatch();
    	
    	if(GE.AreQueuesSameSize())
    		bUserDone = true;
    	
    	gbGameOn = !bUserDone || !bGameOver;
    }	
    
    
    }
    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;

    /** The thread that actually draws the animation */
    private GameGUIThread threadgui;
    private GameEngineThread threadengine;

    Context ct; 
    SurfaceHolder holder;
    private boolean gbGameOn= false;
    
    public SimonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        // register our interest in hearing about changes to our surface
        holder = getHolder();
        holder.addCallback(this);
        ct = context;
        
        // create thread only; it's started in surfaceCreated()
        threadgui = new GameGUIThread(holder, ct, new Handler() {
            @Override
            public void handleMessage(Message m) {
            	// Use for pushing back messages.
            }
        });
        setFocusable(true); // make sure we get key events     
      
    }

    /**
     * Fetches the animation thread corresponding to this LunarView.
     * 
     * @return the animation thread
     */
    public GameGUIThread getGUIThread() {
        return threadgui;
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        //if (!hasWindowFocus)
        //	threadgui.pause();
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        threadgui.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        threadgui.setRunning(true);
        threadgui.start();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        threadgui.setRunning(false);
        while (retry) {
           // try {
                threadgui.interrupt();
                retry = false;
            //} catch (InterruptedException e) {
           // }
        }
    }
    private long inputLast =0;
    
    public boolean onTouchEvent(final MotionEvent event){
		if (gbGameOn) {
			if (event.getEventTime() > inputLast + 200) {
				threadgui.colorDetector((int) event.getX(), (int) event.getY());
				inputLast = event.getEventTime();
			}
		}
		return true;
    }
    
    public void StartGame(){
    	gbGameOn = true;
    	bGameOver = false;
    	bUserDone = false;
    	CreatNewGameEngineThread();
        threadengine.start();
    }
    
    public void EndGame(){
    	gbGameOn = false;
    	threadengine.interrupt();
    }
    
    public void StopThreads(){
    	bGameOver = true;
    	
    	if(threadengine !=null)
    		threadengine.interrupt();
    	threadgui.interrupt();
    }
    
    private void CreatNewGameEngineThread(){
        threadengine = new GameEngineThread(holder, ct, new Handler() {
            @Override
            public void handleMessage(Message m) {
    			AlertDialog adlg = new AlertDialog.Builder(ct)
    			.setTitle("Play Again?")
    			.setMessage("Do you wish to play again?")
    			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                         }
                     }).setNegativeButton("No",  new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                        	 bGameOver = false;
                        	 dialog.dismiss();
                         }
                     })
    			.show();
            }
        });
    }
}
