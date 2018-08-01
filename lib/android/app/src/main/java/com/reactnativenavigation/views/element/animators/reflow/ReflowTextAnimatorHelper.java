/*
 * Copyright 2016 Google Inc.
 * Copyright 2017 Shazam Entertainment Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reactnativenavigation.views.element.animators.reflow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.transition.PathMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.reactnativenavigation.utils.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;
import static android.support.v4.view.ViewCompat.isLaidOut;

/**
 * A transition for repositioning text. This will animate changes in text size and position,
 * re-flowing line breaks as necessary.
 * <p>
 * Strongly recommended to use a curved {@code pathMotion} for a more natural transition.
 */
public final class ReflowTextAnimatorHelper {
    private static final boolean IS_LOLLIPOP_OR_ABOVE = SDK_INT >= LOLLIPOP;
    private static final int TRANSPARENT = 0;
    private static final int OPAQUE = 255;
    private static final int OPACITY_MID_TRANSITION = (int) (0.8f * OPAQUE);
    private static final float STAGGER_DECAY = 0.8f;
    private static final char ELLIPSIS = '…';

    @Nonnull
    private final TextView sourceView;
    @Nonnull
    private final TextView targetView;

    private final boolean showLayers;
    private final long velocity;
    private final long minDuration;
    private final long maxDuration;
    private long staggerDelay;
    private long duration;
    // This is a hack to prevent source view from drawing briefly at the end of the animation :(
    private final boolean freezeOnLastFrame;

    private final AnimatorSet animator = new AnimatorSet();
    private Bitmap startText;
    private Bitmap endText;

    private ReflowTextAnimatorHelper(@Nonnull Builder builder) {
        this.showLayers = builder.showLayers;
        this.sourceView = builder.sourceView;
        this.targetView = builder.targetView;
        this.minDuration = builder.minDuration;
        this.maxDuration = builder.maxDuration;
        this.staggerDelay = builder.staggerDelay;
        this.velocity = builder.velocity;
        this.freezeOnLastFrame = builder.freezeOnLastFrame;
    }

    /**
     * Create an animator to transform between {@code from} and {@code to}, using the configuration defined in the builder.
     * @return An Android Animator. Run or add in an AnimatorSet.
     */
    public Animator createAnimator() {
        duration = calculateDuration(getBounds(sourceView), getBounds(targetView));

        // capture bitmaps of the text
        startText = createBitmap(sourceView);
        endText = createBitmap(targetView);

        // temporarily turn off clipping so we can draw outside of our bounds don't draw
        sourceView.setWillNotDraw(true);
        ((ViewGroup) sourceView.getParent()).setClipChildren(false);

        // calculate the runs of text to move together
        List<Run> runs = getRuns();

        // buildAnimator animators for moving, scaling and fading each run of text
        animator.playTogether(createRunAnimators(sourceView, startText, endText, runs));

        if (!freezeOnLastFrame) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    unfreeze();
                }
            });
        }

        return animator;
    }

    /**
     * Call to recycle bitmaps and allow the {@code from} TextView to keep drawing as normal.
     * Only useful if the animation has been set to freeze on last frame, happens automatically otherwise.
     */
    @SuppressWarnings("WeakerAccess")
    public void unfreeze() {
        sourceView.setWillNotDraw(false);
        sourceView.getOverlay().clear();
        ((ViewGroup) sourceView.getParent()).setClipChildren(true);

        if (startText != null) {
            startText.recycle();
            startText = null;
        }
        if (endText != null) {
            endText.recycle();
            endText = null;
        }
    }

    private static Rect getBounds(View view) {
        int[] loc = new int[2];
        view.getLocationInWindow(loc);
        return new Rect(loc[0], loc[1], loc[0] + view.getWidth(), loc[1] + view.getHeight());
    }

    /**
     * Calculate the {@linkplain Run}s i.e. diff the start and end states, see where text changes
     * line and track the bounds of sections of text that can move together.
     * <p>
     * If a text block has a max number of lines, consider both with and without this limit applied.
     * This allows simulating the correct line breaking as well as calculating the position that
     * overflowing text would have been laid out, so that it can animate from/to that position.
     */
    private List<Run> getRuns() {
        int textLength = Math.max(
                sourceView.getLayout().getLineVisibleEnd(sourceView.getLayout().getLineCount() - 1),
                targetView.getLayout().getLineVisibleEnd(targetView.getLayout().getLineCount() - 1)
        );
        int currentStartLine = 0;
        int currentStartRunLeft = 0;
        int currentStartRunTop = 0;
        int currentEndLine = 0;
        int currentEndRunLeft = 0;
        int currentEndRunTop = 0;
        List<Run> runs = new ArrayList<>();

        Layout startLayout = sourceView.getLayout();
        Layout endLayout = targetView.getLayout();

        int startOffsetLeft = -1;
        int endOffsetLeft = -1;

        char letter;
        int lastCharPosition = 0;
        for (int charPosition = 0; charPosition < textLength; charPosition++) {
            boolean isLastChar = charPosition == textLength - 1;

            // work out which line this letter is on in the start state
            int startLine = startLayout.getLineForOffset(charPosition);
            letter = startLayout.getText().charAt(charPosition);
            if (!isLastChar && letter == ELLIPSIS) {
                // buildAnimator a fake Run to hide '...'
                startLayout = createUnrestrictedLayout(sourceView);
            }

            // work out which line this letter is on in the end state
            int endLine = endLayout.getLineForOffset(charPosition);
            letter = endLayout.getText().charAt(charPosition);
            if (!isLastChar && letter == ELLIPSIS) {
                endLayout = createUnrestrictedLayout(targetView);
            }

            if (startLine != currentStartLine
                    || endLine != currentEndLine
                    || isLastChar) {
                if (isLastChar) {
                    charPosition += 1;
                }

                currentStartLine = Math.min(currentStartLine, startLayout.getLineCount() - 1);
                currentEndLine = Math.min(currentEndLine, endLayout.getLineCount() - 1);

                // at a run boundary, store bounds in both states
                int startRunBottom = startLayout.getLineBottom(currentStartLine);
                int endRunBottom = endLayout.getLineBottom(currentEndLine);

                if (currentStartLine == 0 && startOffsetLeft == -1) {
                    startOffsetLeft = getStartOffsetLeft(startLayout, currentStartLine);
                }
                if (currentEndLine == 0 && endOffsetLeft == -1) {
                    endOffsetLeft = getStartOffsetLeft(endLayout, currentEndLine);
                }

                Rect startBound = new Rect(
                        currentStartRunLeft,
                        currentStartRunTop,
                        currentStartRunLeft + getSectionWidth(startLayout, lastCharPosition, charPosition),
                        startRunBottom);
                startBound.offset(sourceView.getCompoundPaddingLeft() + startOffsetLeft, sourceView.getCompoundPaddingTop());
                Rect endBound = new Rect(
                        currentEndRunLeft,
                        currentEndRunTop,
                        currentEndRunLeft + getSectionWidth(endLayout, lastCharPosition, charPosition),
                        endRunBottom);
                endBound.offset(targetView.getCompoundPaddingLeft() + endOffsetLeft, targetView.getCompoundPaddingTop());
                boolean isStartVisible = startRunBottom <= sourceView.getMeasuredHeight();
                boolean isEndVisible = endRunBottom <= targetView.getMeasuredHeight();
                if (isStartVisible || isEndVisible) {
                    runs.add(new Run(
                            startBound,
                            isStartVisible,
                            endBound,
                            isEndVisible));
                } else {
                    break;
                }
                currentStartLine = startLine;
                currentStartRunLeft = (int) (startLayout.getPrimaryHorizontal(charPosition));
                currentStartRunTop = startLayout.getLineTop(startLine);
                currentEndLine = endLine;
                currentEndRunLeft = (int) (endLayout.getPrimaryHorizontal(charPosition));
                currentEndRunTop = endLayout.getLineTop(endLine);

                startOffsetLeft = 0;
                endOffsetLeft = 0;
                lastCharPosition = charPosition;
            }
        }
        return runs;
    }

    private int getStartOffsetLeft(@Nonnull Layout startLayout, int currentStartLine) {
        return (int) startLayout.getLineLeft(currentStartLine);
    }

    private static int getSectionWidth(@Nonnull Layout layout, int sectionStart, int sectionEnd) {
        CharSequence text = layout.getText();
        TextPaint paint = layout.getPaint();

        return (int) Layout.getDesiredWidth(text, sectionStart, sectionEnd, paint);
    }

    private static Layout createUnrestrictedLayout(@Nonnull TextView view) {
        CharSequence text = view.getText();
        Layout layout = view.getLayout();
        TextPaint paint = layout.getPaint();

        if (SDK_INT >= M) {
            return StaticLayout.Builder
                    .obtain(text, 0, text.length(), layout.getPaint(), layout.getWidth())
                    .setAlignment(layout.getAlignment())
                    .setLineSpacing(view.getLineSpacingExtra(), view.getLineSpacingMultiplier())
                    .setIncludePad(view.getIncludeFontPadding())
                    .setBreakStrategy(view.getBreakStrategy())
                    .setHyphenationFrequency(view.getHyphenationFrequency())
                    .build();
        } else {
            return new StaticLayout(
                    text,
                    paint,
                    text.length(),
                    layout.getAlignment(),
                    view.getLineSpacingMultiplier(),
                    view.getLineSpacingExtra(),
                    view.getIncludeFontPadding());
        }
    }

    private static void drawLayerBounds(@Nonnull Canvas canvas,
                                        @Nonnull Rect bounds,
                                        int sectionNumber,
                                        @Nonnull Paint fillPaint,
                                        @Nonnull Paint outlinePaint,
                                        @Nonnull Paint textPaint) {
        Rect startRect = new Rect(bounds.left + 1, bounds.top + 1, bounds.right - 1, bounds.bottom - 1);
        canvas.drawRect(startRect, fillPaint);
        canvas.drawRect(startRect, outlinePaint);
        canvas.drawText("" + sectionNumber, bounds.left + 6, bounds.top + 21, textPaint);
    }

    /**
     * Create Animators to transition each run of text from start to end position and size.
     */
    @Nonnull
    private List<Animator> createRunAnimators(@Nonnull View view,
                                              @Nonnull Bitmap startText,
                                              @Nonnull Bitmap endText,
                                              @Nonnull List<Run> runs) {
        Rect sourceViewBounds = getBounds(sourceView); // position on the screen of source view
        Rect targetViewBounds = getBounds(targetView); // position on the screen of target view

        List<Animator> animators = new ArrayList<>(runs.size());
        int dx = sourceViewBounds.left - targetViewBounds.left;
        int dy = sourceViewBounds.top - targetViewBounds.top;
        long startDelay = 0L;
        // move text closest to the destination first i.e. loop forward or backward over the runs
        boolean upward = sourceViewBounds.centerY() > targetViewBounds.centerY();
        boolean first = true;
        boolean lastRightward = true;
        LinearInterpolator linearInterpolator = new LinearInterpolator();

        if (showLayers) {
            Paint textPaint = new Paint();
            textPaint.setTextSize(20F);
            textPaint.setColor(Color.BLACK);
            Paint startPaint = new Paint();
            startPaint.setStyle(Paint.Style.STROKE);
            startPaint.setStrokeWidth(1);
            startPaint.setColor(0x80ff0000);
            Paint endPaint = new Paint();
            endPaint.setStyle(Paint.Style.STROKE);
            endPaint.setStrokeWidth(1);
            endPaint.setColor(0x80ff0000);

            int[] colors = sourceView.getResources().getIntArray(com.shazam.android.widget.text.reflow.R.array.debug_colors);
            int color = 0;

            Canvas startCanvas = new Canvas(startText);
            Canvas endCanvas = new Canvas(endText);
            Paint fillPaint = new Paint();
            int section = 1;
            for (Run run : runs) {
                fillPaint.setColor(colors[color++ % colors.length]);
                fillPaint.setAlpha(0x80);

                drawLayerBounds(startCanvas, run.getStart(), section, fillPaint, startPaint, textPaint);
                drawLayerBounds(endCanvas, run.getEnd(), section, fillPaint, startPaint, textPaint);
                section++;
            }
        }

        for (int i = upward ? 0 : runs.size() - 1;
             ((upward && i < runs.size()) || (!upward && i >= 0));
             i += (upward ? 1 : -1)) {
            Run run = runs.get(i);

            // skip text runs which aren't visible in either state
            if (!run.isStartVisible() && !run.isEndVisible()) {
                continue;
            }

            // buildAnimator & position the drawable which displays the run; add it to the overlay.
            SwitchDrawable drawable = new SwitchDrawable(
//                    startText, run.getStart(), sourceView.getTextSize(),
//                    endText, run.getEnd(), targetView.getTextSize());
                    startText, run.getStart(), TextViewUtils.getTextSize(sourceView),
                    endText, run.getEnd(), TextViewUtils.getTextSize(targetView));
            drawable.setBounds(
                    run.getStart().left,
                    run.getStart().top,
                    run.getStart().right,
                    run.getStart().bottom);
            view.getOverlay().add(drawable);

            PropertyValuesHolder topLeft = getPathValuesHolder(run, dy, dx);
            PropertyValuesHolder width = PropertyValuesHolder.ofInt(
                    SwitchDrawable.WIDTH, run.getStart().width(), run.getEnd().width());
            PropertyValuesHolder height = PropertyValuesHolder.ofInt(
                    SwitchDrawable.HEIGHT, run.getStart().height(), run.getEnd().height());
            // the progress property drives the switching behaviour
            PropertyValuesHolder progress = PropertyValuesHolder.ofFloat(
                    SwitchDrawable.PROGRESS, 0f, 1f);
            Animator runAnim = ObjectAnimator.ofPropertyValuesHolder(
                    drawable, topLeft, width, height, progress);

            boolean rightward = run.getStart().centerX() + dx < run.getEnd().centerX();
            if ((run.isStartVisible() && run.isEndVisible())
                    && !first && rightward != lastRightward) {
                // increase the start delay (by a decreasing amount) for the next run
                // (if it's visible throughout) to stagger the movement and try to minimize overlaps
                startDelay += staggerDelay;
                staggerDelay *= STAGGER_DECAY;
            }
            lastRightward = rightward;
            first = false;

            runAnim.setStartDelay(startDelay);
            long animDuration = Math.max(minDuration, duration - (startDelay / 2));
            runAnim.setDuration(animDuration);
            animators.add(runAnim);

            if (run.isStartVisible() != run.isEndVisible()) {
                // if run is appearing/disappearing then fade it in/out
                ObjectAnimator fade = ObjectAnimator.ofInt(
                        drawable,
                        SwitchDrawable.ALPHA,
                        run.isStartVisible() ? OPAQUE : TRANSPARENT,
                        run.isEndVisible() ? OPAQUE : TRANSPARENT);
                fade.setDuration((duration + startDelay) / 2);
                if (!run.isStartVisible()) {
                    drawable.setAlpha(TRANSPARENT);
                    fade.setStartDelay((duration + startDelay) / 2);
                } else {
                    fade.setStartDelay(startDelay);
                }
                animators.add(fade);
            } else {
                // slightly fade during transition to minimize movement
                ObjectAnimator fade = ObjectAnimator.ofInt(
                        drawable,
                        SwitchDrawable.ALPHA,
                        OPAQUE, OPACITY_MID_TRANSITION, OPAQUE);
                fade.setStartDelay(startDelay);
                fade.setDuration(duration + startDelay);
                fade.setInterpolator(linearInterpolator);
                animators.add(fade);
            }
        }
        return animators;
    }

    private static Path getPath(float startX, float startY, float endX, float endY) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
        return path;
    }

    @TargetApi(LOLLIPOP)
    private PropertyValuesHolder getPathValuesHolder(Run run, int dy, int dx) {
        PropertyValuesHolder propertyValuesHolder;
        if (IS_LOLLIPOP_OR_ABOVE) {
            PathMotion pathMotion = new PathMotion() {
                @Override
                public Path getPath(float startX, float startY, float endX, float endY) {
                    return ReflowTextAnimatorHelper.getPath(startX, startY, endX, endY);
                }
            };
            propertyValuesHolder = PropertyValuesHolder.ofObject(SwitchDrawable.TOP_LEFT, null,
                    pathMotion.getPath(
                            run.getStart().left,
                            run.getStart().top,
                            run.getEnd().left - dx,
                            run.getEnd().top - dy));
        } else {
            PointF startPoint = new PointF(run.getStart().left, run.getStart().top);
            PointF endPoint = new PointF(run.getEnd().left - dx, run.getEnd().top - dy);
            propertyValuesHolder = PropertyValuesHolder.ofObject(SwitchDrawable.TOP_LEFT, new TypeEvaluator<PointF>() {
                private final PointF point = new PointF();

                @Override
                public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                    float x = startValue.x + (endValue.x - startValue.x) * fraction;
                    float y = startValue.y + (endValue.y - startValue.y) * fraction;

                    point.set(x, y);

                    return point;
                }
            }, startPoint, endPoint);
        }

        return propertyValuesHolder;
    }

    private Bitmap createBitmap(@Nonnull View view) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight() * (showLayers ? 3 : 1);
        Bitmap bitmap = Bitmap.createBitmap(width, height, ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * Calculate the withDuration for the transition depending upon how far the text has to move.
     */
    private long calculateDuration(@Nonnull Rect startPosition, @Nonnull Rect endPosition) {
        float distance = (float) Math.hypot(
                startPosition.exactCenterX() - endPosition.exactCenterX(),
                startPosition.exactCenterY() - endPosition.exactCenterY());
        long duration = (long) (1_000L * (distance / velocity));
        return Math.max(minDuration, Math.min(maxDuration, duration));
    }

    public static class Builder {
        private static final long DEFAULT_VELOCITY = 700L;
        private static final long DEFAULT_MIN_DURATION = 200L;
        private static final long DEFAULT_MAX_DURATION = 400L;
        private static final long DEFAULT_STAGGER = 40L;

        private TextView sourceView;
        private TextView targetView;
        private boolean showLayers = false;
        private long minDuration = DEFAULT_MIN_DURATION;
        private long maxDuration = DEFAULT_MAX_DURATION;
        private long staggerDelay = DEFAULT_STAGGER;
        private long velocity = DEFAULT_VELOCITY;
        private boolean freezeOnLastFrame = false;

        /**
         * @param from This View will be transformed to look like {@code to}.
         * @param to The View {@code from} will match at the end of the animation.
         */
        public Builder(@Nonnull TextView from, @Nonnull TextView to) {
            this.sourceView = from;
            this.targetView = to;

            //noinspection ConstantConditions
            if (sourceView == null) {
                throw new IllegalArgumentException("Source view can't be null");
            } else if (!isLaidOut(sourceView)) {
                throw new IllegalArgumentException("Source view not laid out. Consider calling this in an OnPreDrawListener!");
            }
            if (targetView == null) {
                throw new IllegalArgumentException("Target view can't be null");
            } else if (!isLaidOut(targetView)) {
                throw new IllegalArgumentException("Target view not laid out. Consider calling this in an OnPreDrawListener!");
            }
        }

        /**
         * @param showLayers Pass true to draw a bounding box for each text section.
         * @return {@link Builder}
         */
        public Builder debug(boolean showLayers) {
            this.showLayers = showLayers;
            return this;
        }

        /**
         * Set animation duration bounds. The actual duration is calculated based on the velocity.
         * @param minDurationMs Default is {@value DEFAULT_MIN_DURATION}.
         * @param maxDurationMs Default is {@value DEFAULT_MAX_DURATION}.
         * @return {@link Builder}
         */
        public Builder withDuration(long minDurationMs, long maxDurationMs) {
            this.minDuration = minDurationMs;
            this.maxDuration = maxDurationMs;
            return this;
        }

        /**
         * @param staggerDelayMs Time by which each moving part will be staggered to reduce overlap.
         *                       The real stagger duration for each part is decaying, so this is only a starting value. Default is {@value DEFAULT_STAGGER}.
         * @return {@link Builder}
         */
        public Builder withStaggerDelay(long staggerDelayMs) {
            this.staggerDelay = staggerDelayMs;
            return this;
        }

        /**
         * Velocity governs the duration of the produced Animator.
         * @param velocity pixels per second. Defaults to {@value DEFAULT_VELOCITY}.
         * @return {@link Builder}
         * @see #withDuration(long, long)
         */
        public Builder withVelocity(long velocity) {
            this.velocity = velocity;
            return this;
        }

        /**
         * @param freezeOnLastFrame Passing true will cause the origin TextView to freeze on the last frame of the transformation and stop updating. You will need to call {@link #unfreeze()} yourself to clean up and return to normal!
         * @return {@link Builder}
         */
        public Builder withFreezeOnLastFrame(boolean freezeOnLastFrame) {
            this.freezeOnLastFrame = freezeOnLastFrame;
            return this;
        }

        /**
         * @return A configured {@link ReflowTextAnimatorHelper} instance. Use it to produce
         * Android animators for your transformation by calling {@link #createAnimator()}.
         */
        public ReflowTextAnimatorHelper build() {
            return new ReflowTextAnimatorHelper(this);
        }

        /**
         * Convenience alias for build().createAnimator().
         * Use if you don't need to freeze last frame (and therefore manually {@link #unfreeze()} later).
         * @throws IllegalStateException if freezeOnLastFrame is enabled.
         * @return {@link Animator}
         */
        public Animator buildAnimator() {
            if (freezeOnLastFrame) {
                throw new IllegalStateException("Use build() to retain ability to unfreeze() the Helper.");
            }
            return build().createAnimator();
        }
    }
}
