package se.lth.mamn01.jogr.jogr;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


/**
 * Listener that detects shake gesture.
 */
public class ShakeEventListener implements SensorEventListener {

    int direction = 0;
    /**
     * Minimum movement force to consider.
     */
    private static final int MIN_FORCE_X = 5;
    private static final int MIN_FORCE_Z = 7;

    private static final int MIN_TIME_BETWEEN_SHAKES = 500;
    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */
    private static final int MIN_DIRECTION_CHANGE = 2;

    /**
     * Maximum pause between movements.
     */
    private static final int MAX_PAUSE_BETWEEN_DIRECTION_CHANGE = 200;

    /**
     * Maximum allowed time for shake gesture.
     */
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;

    /**
     * Time when the gesture started.
     */
    private long mFirstDirectionChangeTime = 0;

    /**
     * Time when the last movement started.
     */
    private long mLastDirectionChangeTime;

    /**
     * How many movements are considered so far.
     */
    private int mDirectionChangeCount = 0;

    /**
     * The last x position.
     */
    private float lastX = 0;

    /**
     * The last y position.
     */
    private float lastY = 0;

    /**
     * The last z position.
     */
    private float lastZ = 0;

    private long blockTime = 0;
    /**
     * OnShakeListener that is called when shake is detected.
     */
    private OnShakeListener mShakeListener;


    public ShakeEventListener() {

    }

    /**
     * Interface for shake gesture.
     */
    public interface OnShakeListener {
        /**
         * Called when shake gesture is detected.
         */
        void onShakeLeft();

        void onShakeRight();

        void onShakeForward();

        void onShakeBack();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
    }


    //Denna kod verkar köras även när sensorn inte ändras. Spelar dock ingen roll, så länge vi läser av värdena på accelerometern.
    @Override
    public void onSensorChanged(SensorEvent se) {
        // get sensor data
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];


        // calculate movement
        float totalMovementX = Math.abs(x - lastX);
        // float totalMovementZ = Math.abs(z - lastZ);


        if (totalMovementX > MIN_FORCE_X) {

            //För test
            // mShakeListener.onShake();
            // resetShakeParameters();


            // get time
            long now = System.currentTimeMillis();

            // store first movement time
            if (mFirstDirectionChangeTime == 0) {
                if (System.currentTimeMillis() - blockTime <= MIN_TIME_BETWEEN_SHAKES) {
                    return;
                }

                mFirstDirectionChangeTime = now;
                mLastDirectionChangeTime = now;
                /*if(totalMovementZ > MIN_FORCE_Z){
                    if(z-lastZ >= 0){
                        direction = 3;
                    }
                    else{
                        direction = 4;
                    }
                }*/
                if (totalMovementX > MIN_FORCE_X) {


                    if (x - lastX >= 0) {
                        direction = 1;
                    } else {
                        direction = 2;
                    }
                }


            }

            // check if the last movement was not long ago
            long lastChangeWasAgo = now - mLastDirectionChangeTime;
            if (lastChangeWasAgo < MAX_PAUSE_BETWEEN_DIRECTION_CHANGE) {

                // store movement data
                mLastDirectionChangeTime = now;
                mDirectionChangeCount++;

                // store last sensor data
                lastX = x;
                lastY = y;
                lastZ = z;

                // check how many movements are so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    // check total duration
                    long totalDuration = now - mFirstDirectionChangeTime;
                    if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {

                        blockTime = System.currentTimeMillis();

                        if (direction == 1) {

                            mShakeListener.onShakeRight();
                        } else if (direction == 2) {

                            mShakeListener.onShakeLeft();
                        } else if (direction == 4) {

                            mShakeListener.onShakeForward();
                        } else if (direction == 3) {

                            mShakeListener.onShakeBack();
                        }
                        resetShakeParameters();
                        //lastZ = se.values[2];

                    }
                }

            } else {
                resetShakeParameters();
                // lastZ = se.values[2];
            }
        }
    }

    /**
     * Resets the shake parameters to their default values.
     */
    private void resetShakeParameters() {
        mFirstDirectionChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;

        direction = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}