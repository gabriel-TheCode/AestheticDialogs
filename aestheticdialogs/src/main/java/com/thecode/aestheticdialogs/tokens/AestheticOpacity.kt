package com.thecode.aestheticdialogs.tokens

/**
 * Opacity scale.
 *
 * Short on purpose: the library separates surfaces with colour and elevation, not
 * with transparency. These are the two places where a value is drawn *through*
 * another one and needs a name rather than a literal.
 */
public object AestheticOpacity {
    /** The unfilled part of a progress bar drawn on a toned surface. */
    public val track: Float = 0.24f
}
