package com.reactnativenavigation.viewcontrollers.button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.reactnativenavigation.R;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;
import com.reactnativenavigation.utils.Task;

public class NavigationIconResolver {

    private Context context;
    private ImageLoader imageLoader;

    public NavigationIconResolver(Context context, ImageLoader imageLoader) {
        this.context = context;
        this.imageLoader = imageLoader;
    }

    public void resolve(Button button, Task<Drawable> onSuccess) {
        if (button.icon.hasValue()) {
            imageLoader.loadIcon(context, button.icon.get(), new ImageLoadingListenerAdapter() {
                @Override
                public void onComplete(@NonNull Drawable icon) {
                    onSuccess.run(icon);
                }

                @Override
                public void onError(Throwable error) {
                    throw new RuntimeException(error);
                }
            });
        } else if (Constants.BACK_BUTTON_ID.equals(button.id)) {
            onSuccess.run(ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_black_24dp));
        } else {
            Log.w("RNN", "Left button needs to have an icon");
        }
    }
}
