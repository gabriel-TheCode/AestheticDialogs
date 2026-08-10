package com.thecode.aestheticdialogs.tokens

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable

/**
 * Motion tokens.
 *
 * [enabled] is the reduced-motion switch. It is resolved from the platform
 * animation scale by default (see
 * [com.thecode.aestheticdialogs.foundation.AestheticDialogsTheme]) and can be
 * forced by the application. When it is `false`, every transition collapses to
 * an instant cut instead of being merely shortened, which is what the
 * "remove animations" accessibility setting actually asks for.
 */
@Immutable
public data class AestheticMotion(
    /** The reduced-motion switch. When `false`, every transition becomes an instant cut. */
    val enabled: Boolean = true,
    /** Duration of a leaving transition, in milliseconds. */
    val durationFast: Int = 120,
    /** Duration of an arriving transition, in milliseconds. */
    val durationMedium: Int = 220,
    /** Duration reserved for transitions that cross the whole screen, in milliseconds. */
    val durationSlow: Int = 320,
    /** Easing of an arriving transition: decelerating, so the surface settles. */
    val easingEnter: Easing = LinearOutSlowInEasing,
    /** Easing of a leaving transition: accelerating, so it gets out of the way. */
    val easingExit: Easing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f),
) {
    /**
     * Spec for content appearing: medium duration, decelerating.
     *
     * @param T the animated value type, inferred from the transition it is passed to.
     * @return a tween, or a zero-duration cut when [enabled] is `false`.
     */
    public fun <T> enterSpec(): FiniteAnimationSpec<T> =
        tween(durationMillis = if (enabled) durationMedium else 0, easing = easingEnter)

    /**
     * Spec for content leaving: fast duration, accelerating.
     *
     * @param T the animated value type, inferred from the transition it is passed to.
     * @return a tween, or a zero-duration cut when [enabled] is `false`.
     */
    public fun <T> exitSpec(): FiniteAnimationSpec<T> =
        tween(durationMillis = if (enabled) durationFast else 0, easing = easingExit)

    public companion object {
        public val Default: AestheticMotion = AestheticMotion()
    }
}

/**
 * How a notification banner enters and leaves the screen.
 *
 * The banner host owns the visibility of its notifications, so it is the one
 * place in the library that can run a real exit animation. Modal dialogs are
 * shown and removed by the caller, so they animate in only — see
 * `docs/ARCHITECTURE.md`.
 */
public enum class AestheticNotificationAnimation {
    /** Slides from the anchored edge. The default: it shows where the banner came from. */
    Slide,

    /** Fades in place. The quietest option, and the closest to "no motion". */
    Fade,

    /** Scales up from 92%, combined with a fade. */
    Scale,

    /** Slides in from the start edge. */
    SlideHorizontal,

    /** No transition at all. */
    None,
}
