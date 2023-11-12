import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs

class CollapsibleHeaderScaffoldState(
    initialHeaderHeightOffsetLimit: Float,
    initialHeaderHeightOffset: Float,
    private val flingAnimationSpec: DecayAnimationSpec<Float>?,
    private val snapAnimationSpec: AnimationSpec<Float>?
) {
    private var _headerHeightOffset = mutableFloatStateOf(initialHeaderHeightOffset)

    var headerHeightOffset: Float
        get() = _headerHeightOffset.floatValue
        set(newOffset) {
            _headerHeightOffset.floatValue = newOffset.coerceIn(
                minimumValue = headerHeightOffsetLimit,
                maximumValue = 0.0f
            )
        }

    private var _headerHeightOffsetLimit = mutableFloatStateOf(initialHeaderHeightOffsetLimit)

    var headerHeightOffsetLimit: Float
        get() = _headerHeightOffsetLimit.floatValue
        set(newLimit) {
            _headerHeightOffsetLimit.floatValue = newLimit.coerceAtMost(
                maximumValue = 0.0f
            )
        }

    val headerCollapsedFraction: Float
        get() = headerHeightOffsetLimit.let { limit ->
            if (limit != 0.0f) {
                headerHeightOffset / limit
            } else {
                0.0f
            }
        }

    private val isHeaderExpanded: Boolean
        // Note that we don't check for 0f due to float precision with the collapsedFraction
        // calculation.
        get() {
            return headerCollapsedFraction < 0.01f
        }

    private val isHeaderCollapsed
        get() = headerCollapsedFraction == 1.0f

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            // If user is scrolling back up to top, allow list to consume delta
            if (available.y > 0) return Offset.Zero
            val prevHeaderHeightOffset = headerHeightOffset
            headerHeightOffset += available.y
            // Because of the coerce we have in place, we may only consume a capped amount
            // of the available delta.
            return if (prevHeaderHeightOffset != headerHeightOffset) {
                available.copy(x = 0.0f)
            } else {
                Offset.Zero
            }
        }

        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
            // We're scrolling back up and there's available delta, which means the list has fully
            // consumed what it can. Let's consume the rest of what we can up to our limit.
            if (available.y > 0) {
                val prevHeaderHeightOffset = headerHeightOffset
                headerHeightOffset += available.y
                return Offset(
                    x = 0.0f,
                    y = headerHeightOffset - prevHeaderHeightOffset
                )
            }
            return Offset.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            val superConsumed = super.onPostFling(consumed, available)
            return superConsumed + settleHeader(available.y)
        }
    }

    /**
     * Common fling handling functionality provided by https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/AppBar.kt;l=2108?q=TopAppBarState
     */
    private suspend fun settleHeader(
        velocity: Float
    ): Velocity {
        // Check if the header is completely collapsed/expanded. If so, no need to settle the header,
        // and just return Zero Velocity.
        if (isHeaderCollapsed || isHeaderExpanded) {
            return Velocity.Zero
        }
        var remainingVelocity = velocity
        // In case there is an initial velocity that was left after a previous user fling, animate to
        // continue the motion to expand or collapse the header.
        if (flingAnimationSpec != null && abs(velocity) > 1f) {
            var lastValue = 0f
            AnimationState(
                initialValue = 0f,
                initialVelocity = velocity
            ).animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeaderHeightOffset = headerHeightOffset
                headerHeightOffset = initialHeaderHeightOffset + delta
                val consumed = abs(initialHeaderHeightOffset - headerHeightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
        }
        if (snapAnimationSpec != null) {
            if (headerHeightOffset < 0 && headerHeightOffset > headerHeightOffsetLimit) {
                AnimationState(initialValue = headerHeightOffset).animateTo(
                    if (headerCollapsedFraction < 0.5f) {
                        0f
                    } else {
                        headerHeightOffsetLimit
                    },
                    animationSpec = snapAnimationSpec
                ) { headerHeightOffset = value }
            }
        }

        return Velocity(0f, remainingVelocity)
    }

    companion object {
        fun Saver(
            snapAnimationSpec: AnimationSpec<Float>?,
            flingAnimationSpec: DecayAnimationSpec<Float>?
        ): Saver<CollapsibleHeaderScaffoldState, *> = listSaver(
            save = { listOf(it.headerHeightOffsetLimit, it.headerHeightOffset) },
            restore = {
                CollapsibleHeaderScaffoldState(
                    initialHeaderHeightOffsetLimit = it[0],
                    initialHeaderHeightOffset = it[1],
                    snapAnimationSpec = snapAnimationSpec,
                    flingAnimationSpec = flingAnimationSpec
                )
            }
        )
    }
}

@Composable
fun rememberCollapsibleHeaderScaffoldState(
    initialHeaderHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeaderHeightOffset: Float = 0.0f,
    snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
    flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay()
) = rememberSaveable(
    snapAnimationSpec,
    flingAnimationSpec,
    saver = CollapsibleHeaderScaffoldState.Saver(
        snapAnimationSpec = snapAnimationSpec,
        flingAnimationSpec = flingAnimationSpec
    )
) {
    CollapsibleHeaderScaffoldState(
        initialHeaderHeightOffsetLimit = initialHeaderHeightOffsetLimit,
        initialHeaderHeightOffset = initialHeaderHeightOffset,
        snapAnimationSpec = snapAnimationSpec,
        flingAnimationSpec = flingAnimationSpec
    )
}
