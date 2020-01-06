package com.reactnativenavigation.viewcontrollers.button;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;

import androidx.annotation.NonNull;

public class IconResolver {

    private Activity context;
    private ImageLoader imageLoader;

    public IconResolver(Activity context, ImageLoader imageLoader) {
        this.context = context;
        this.imageLoader = imageLoader;
    }

    public void resolve(Button button, Func1<Drawable> onSuccess) {
        if (button.hasIcon()) {
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
            onSuccess.run(imageLoader.getBackButtonIcon(context));
        } else {
            Log.w("RNN", "Left button needs to have an icon");
        }
    }
}
