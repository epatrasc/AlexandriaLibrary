package com.alexandria.android.alexandrialibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alexandria.android.alexandrialibrary.asynctask.CatalogoLibriTask;
import com.alexandria.android.alexandrialibrary.asynctask.PrestitiTask;

public class PrestitiActivity extends AppCompatActivity {
    private View mProgressView;
    private ListView prestitiView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestiti);

        mProgressView = findViewById(R.id.prestiti_progress);
        prestitiView = (ListView) findViewById(R.id.prestiti_list);

        // prepara catalogo
        PrestitiTask task = new PrestitiTask(this);
        task.execute();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            prestitiView.setVisibility(show ? View.GONE : View.VISIBLE);
            prestitiView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    prestitiView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            prestitiView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public ListView getPrestitiView() {
        return prestitiView;
    }

    public void setPrestitiView(ListView prestitiView) {
        this.prestitiView = prestitiView;
    }
}
