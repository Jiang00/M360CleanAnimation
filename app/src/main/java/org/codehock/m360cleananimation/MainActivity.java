package org.codehock.m360cleananimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.codehock.cleananimation.M360CleanAnimationView;

public class MainActivity extends AppCompatActivity {

    private Button mStart;
    private M360CleanAnimationView mM360animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStart = (Button) findViewById(R.id.start);
        mM360animation = (M360CleanAnimationView) findViewById(R.id.m360animation);

        mM360animation.setCleanNumber(90);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mM360animation.startAnimation();
            }
        });
    }
}
