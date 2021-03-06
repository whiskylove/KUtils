package com.kiven.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kiven.kutils.activityHelper.KHelperActivity;
import com.kiven.kutils.custom.BaseHelper;
import com.kiven.sample.floatView.ActivityHFloatView;

import roboguice.inject.InjectView;

/**
 *
 * Created by kiven on 16/5/6.
 */
public class ActivityHTestBase extends BaseHelper {
    @InjectView(R.id.button1) private Button button;

    @Override
    public void onCreate(KHelperActivity activity, Bundle savedInstanceState) {
        super.onCreate(activity, savedInstanceState);

        setContentView(R.layout.activity_lauch);

        button.setText("RoboHelper Test");
    }

    boolean visible = true;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:

                /*AppContext.getInstance().startSinkActivity(new ActivityHFloatView());*/
                break;
            case R.id.button2:
//                TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.root), new Slide(Gravity.LEFT));
                visible = !visible;
                findViewById(R.id.button1).setVisibility(visible? View.VISIBLE: View.GONE);
                break;
        }
    }
}
