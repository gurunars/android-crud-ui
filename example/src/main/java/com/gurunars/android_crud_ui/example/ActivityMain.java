package com.gurunars.android_crud_ui.example;

import android.graphics.Color;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gurunars.utils.storage.PersistentStorage;
import com.gurunars.utils.ui.AutoBg;
import com.gurunars.utils.ui.ColoredShapeDrawable;
import com.gurunars.utils.ui.ViewFinder;


public class ActivityMain extends AppCompatActivity {

    private TextView payloadView;
    private PersistentStorage<TestPayload> storage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        payloadView = ViewFinder.findViewById(this, R.id.payload);
        storage = new PersistentStorage<>(this, "payloadAlias", new TestPayload("Empty"),
                new PersistentStorage.PayloadChangeListener<TestPayload>() {
                    @Override
                    public void onChange(TestPayload payload) {
                        payloadView.setText(payload.title);
                    }
        });

        View set = ViewFinder.findViewById(this, R.id.set);
        set.setBackground(new ColoredShapeDrawable(new OvalShape(), Color.YELLOW));
        AutoBg.apply(set, 6);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.set(new TestPayload("Configured"));
            }
        });

        View clear = ViewFinder.findViewById(this, R.id.clear);
        AutoBg.apply(clear, 6);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.clear();
            }
        });

        AutoBg.apply(ViewFinder.findViewById(this, R.id.disabled), 6);
    }

}