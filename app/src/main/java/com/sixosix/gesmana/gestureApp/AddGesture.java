package com.sixosix.gesmana.gestureApp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sixosix.gesmana.MainActivity;
import com.sixosix.gesmana.R;
import com.sixosix.gesmana.utils.BaseActivity;
import com.sixosix.gesmana.utils.FinalName;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by oops on 18-01-02.
 */


public class AddGesture extends BaseActivity {
    private SharedPreferences preferences = MainActivity.preferences;
    private SharedPreferences.Editor editor = MainActivity.editor;
    private GestureOverlayView gestureOverlayView;
    private Set<String> names;
    private Bundle bundle;
    private Set<String> packages;

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gesture);
        bundle = getIntent().getBundleExtra("package");
        final String packageName = bundle.getString("packageName");
        final String packagesName = bundle.getString("packages");
        gestureOverlayView = findViewById(R.id.gesture_add);
        gestureOverlayView.setGestureColor(R.color.colorAccent);
        gestureOverlayView.setGestureStrokeWidth(4);
        gestureOverlayView.addOnGesturePerformedListener(
                new GestureOverlayView.OnGesturePerformedListener() {
                    @Override
                    public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {
                        View saveDialog = getLayoutInflater().inflate(R.layout.activity_save_gesture, null);
                        ImageView imageView = saveDialog.findViewById(R.id.show);
                        Bitmap bitmap = gesture.toBitmap(128, 128, 10, 0xffff0000);
                        imageView.setImageBitmap(bitmap);
                        new AlertDialog.Builder(AddGesture.this)
                                .setView(saveDialog)
                                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String gesturePath = getFilesDir().getPath() + File.separator + FinalName.GESTURE_PATH;
                                        GestureLibrary library = GestureLibraries.fromFile(gesturePath);
                                        library.load();
                                        names = library.getGestureEntries();
                                        if (!names.contains(packageName)) {
                                            packages = preferences.getStringSet(packagesName, new HashSet<String>());
                                            packages.add(packageName);
                                            editor.putStringSet(packagesName, packages);
                                            editor.commit();
                                            library.addGesture(packageName, gesture);
                                            library.save();
                                            Toast.makeText(AddGesture.this, "设置成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AddGesture.this, "设置失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                }
        );

    }

    @Override
    public void back(View view) {
        finish();
    }
}
