package com.github.florent37.mylittlecanvas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShapeAnimator {
    private View view;
    private List<ValueAnimator> animators = new ArrayList<>();
    private int repeatCount = 0;
    private long duration = 300;
    private long startDelay = 0;

    private Interpolator interpolator = new LinearInterpolator();

    private List<OnAnimationStart> onAnimationStarts = new ArrayList<>();
    private List<OnAnimationEnd> onAnimationEnds = new ArrayList<>();
    private AtomicInteger endedAnimationsCount = new AtomicInteger(0);

    public ShapeAnimator(@NonNull View view) {
        this.view = view;
    }

    public ShapeAnimator(@NonNull View view, List<ValueAnimator> animators) {
        this(view);
        playTogether(animators);
    }

    public ShapeAnimator(@NonNull View view, ValueAnimator... animators) {
        this(view);
        playTogether(animators);
    }

    public void clear() {
        for (ValueAnimator animator : animators) {
            animator.cancel();
        }
        animators.clear();
    }

    public ShapeAnimator playTogether(List<ValueAnimator> animators) {
        this.animators.addAll(animators);
        return this;
    }

    public ShapeAnimator playTogether(ValueAnimator... animators) {
        if (animators != null) {
            this.animators.addAll(Arrays.asList(animators));
        }
        return this;
    }

    public ShapeAnimator setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public ShapeAnimator start(@Nullable final OnAnimationEnd onAnimationEnd) {
        this.onAnimationEnd(onAnimationEnd);
        return start();
    }

    public ShapeAnimator start() {
        endedAnimationsCount.set(0);

        for (OnAnimationStart onAnimationStart : onAnimationStarts) {
            onAnimationStart.onAnimationStart();
        }
        //do not use AnimatorSet because you cannot use setRepeatCount

        final int animationCount = animators.size();

        for (ValueAnimator animator : animators) {
            animator.setRepeatCount(repeatCount);
            animator.setDuration(duration);
            animator.setInterpolator(interpolator);
            animator.setStartDelay(startDelay);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (view != null) {
                        view.postInvalidate();
                    }
                }
            });
            animator.addListener(
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            final int finishedAnims = endedAnimationsCount.incrementAndGet();
                            if(animationCount == finishedAnims) {
                                for (OnAnimationEnd onAnimationEnd : onAnimationEnds) {
                                    onAnimationEnd.onAnimationEnd();
                                }
                            }
                        }
                    }
            );
            animator.start();
        }
        return this;
    }

    public ShapeAnimator setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    public ShapeAnimator setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public ShapeAnimator setStartDelay(long startDelay) {
        this.startDelay = startDelay;
        return this;
    }

    public ShapeAnimator onAnimationEnd(@Nullable final OnAnimationEnd onAnimationEnd) {
        if (onAnimationEnd != null) {
            this.onAnimationEnds.add(onAnimationEnd);
        }
        return this;
    }

    public ShapeAnimator onAnimationStart(@Nullable final OnAnimationStart onAnimationStart) {
        if (onAnimationStart != null) {
            this.onAnimationStarts.add(onAnimationStart);
        }
        return this;
    }

    public interface OnAnimationStart {
        void onAnimationStart();
    }

    public interface OnAnimationEnd {
        void onAnimationEnd();
    }
}
