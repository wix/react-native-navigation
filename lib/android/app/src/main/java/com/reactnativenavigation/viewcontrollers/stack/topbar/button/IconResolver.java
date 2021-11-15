package com.reactnativenavigation.viewcontrollers.stack.topbar.button;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

public class IconResolver {

    private Activity context;
    private ImageLoader imageLoader;

    public IconResolver(Activity context, ImageLoader imageLoader) {
        this.context = context;
        this.imageLoader = imageLoader;
    }

    public void resolve(ButtonOptions button, Func1<Drawable> onSuccess) {
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
        } else if (button.isBackButton()) {
            onSuccess.run(imageLoader.getBackButtonIcon(context));
        } else {
            Log.w("RNN", "Left button needs to have an icon");
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
