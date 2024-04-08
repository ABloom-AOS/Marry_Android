package com.abloom.mery.presentation.common.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.use
import androidx.databinding.BindingAdapter
import com.abloom.mery.R
import com.abloom.mery.presentation.common.extension.dp
import kotlin.properties.Delegates.observable

class MeryAppBar(
    context: Context,
    attrs: AttributeSet,
) : ConstraintLayout(context, attrs) {

    private var navigationView: View? by observable(null) { _, _, newValue ->
        if (newValue == null) return@observable
        newValue.setOnClickListener(onNavigationClickListener)
        addView(newValue)
        newValue.applyNavigationConstraint()
    }
    private var titleView: TextView? by observable(null) { _, _, newValue ->
        if (newValue == null) return@observable
        addView(newValue)
        newValue.applyTitleConstraint()
    }
    private var actionView: TextView? by observable(null) { _, _, newValue ->
        if (newValue == null) return@observable
        newValue.setOnClickListener(onActionClickListener)
        addView(newValue)
        newValue.applyActionConstraint()
    }

    var navigationIcon: Drawable? by observable(null) { _, _, newValue ->
        if (navigationView != null) removeView(navigationView)
        if (newValue == null) return@observable
        navigationView = createNavigationIconView(newValue)
    }

    var navigationText: CharSequence? by observable(null) { _, _, newValue ->
        if (navigationView != null) removeView(navigationView)
        if (newValue == null) return@observable
        navigationView = createNavigationTextView(newValue)
    }

    var title: CharSequence by observable("") { _, _, newValue ->
        if (titleView == null) {
            titleView = createTitleView(newValue)
            return@observable
        }
        titleView!!.text = newValue
    }

    var actionText: CharSequence by observable("") { _, _, newValue ->
        if (actionView == null) {
            actionView = createActionView(newValue)
            return@observable
        }
        actionView!!.text = newValue
    }

    var isActionEnabled: Boolean by observable(true) { _, _, newValue ->
        actionView?.isEnabled = newValue
    }

    var onNavigationClickListener: OnClickListener? by observable(null) { _, _, newValue ->
        navigationView?.setOnClickListener(newValue)
    }

    var onActionClickListener: OnClickListener? by observable(null) { _, _, newValue ->
        actionView?.setOnClickListener(newValue)
    }

    init {
        setupView()
        setupChildViews(attrs)
    }

    private fun setupView() {
        minHeight = APP_BAR_HEIGHT
        maxHeight = APP_BAR_HEIGHT
        setPadding(APP_BAR_HORIZONTAL_PADDING, 0, APP_BAR_HORIZONTAL_PADDING, 0)
    }

    private fun setupChildViews(attrs: AttributeSet) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.MeryAppBar,
            0,
            0
        ).use { typedArray ->
            setupNavigation(typedArray)

            titleView =
                typedArray.getText(R.styleable.MeryAppBar_title)?.let { createTitleView(it) }

            actionView =
                typedArray.getText(R.styleable.MeryAppBar_action)?.let { createActionView(it) }
        }
    }

    private fun setupNavigation(typedArray: TypedArray) {
        val navigationIcon = typedArray.getDrawable(R.styleable.MeryAppBar_navigationIcon)
        val navigationText = typedArray.getText(R.styleable.MeryAppBar_navigationText)
        require(!(navigationIcon != null && navigationText != null)) { "MeryAppBar의 navigationIcon과 navigationText 둘 중 하나만 사용할 수 있습니다." }
        navigationView = navigationIcon?.let { createNavigationIconView(it) }
            ?: navigationText?.let { createNavigationTextView(it) }
    }

    private fun createNavigationIconView(drawable: Drawable) = ImageView(context).apply {
        id = generateViewId()
        layoutParams = LayoutParams(NAVIGATION_ICON_BUTTON_SIZE, NAVIGATION_ICON_BUTTON_SIZE)
        setImageDrawable(drawable)
    }

    private fun createNavigationTextView(text: CharSequence) = TextView(context).apply {
        id = generateViewId()
        this.text = text
        setTextAppearance(R.style.callout)
        typeface = ResourcesCompat.getFont(context, R.font.nanum_square_neo_bold)
        val color = ContextCompat.getColor(context, R.color.primary_80)
        setTextColor(color)
        setPadding(
            BUTTON_HORIZONTAL_PADDING,
            BUTTON_VERTICAL_PADDING,
            BUTTON_HORIZONTAL_PADDING,
            BUTTON_VERTICAL_PADDING
        )
    }

    private fun createTitleView(text: CharSequence) = TextView(context).apply {
        id = generateViewId()
        this.text = text
        setTextAppearance(R.style.body)
        typeface = ResourcesCompat.getFont(context, R.font.nanum_square_neo_bold)
        setTextColor(Color.BLACK)
    }

    private fun createActionView(text: CharSequence) = TextView(context).apply {
        id = generateViewId()
        this.text = text
        setTextAppearance(R.style.callout)
        typeface = ResourcesCompat.getFont(context, R.font.nanum_square_neo_bold)
        val colors = ContextCompat.getColorStateList(context, R.color.text_button)
        setTextColor(colors)
        setPadding(
            BUTTON_HORIZONTAL_PADDING,
            BUTTON_VERTICAL_PADDING,
            BUTTON_HORIZONTAL_PADDING,
            BUTTON_VERTICAL_PADDING
        )
    }

    private fun View.applyNavigationConstraint() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this@MeryAppBar)

        constraintSet.constrainWidth(this.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(this.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(
            this.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            this.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            this.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(this@MeryAppBar)
    }

    private fun View.applyTitleConstraint() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this@MeryAppBar)

        constraintSet.constrainWidth(this.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(this.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(
            this.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            this.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            this.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            this.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(this@MeryAppBar)
    }

    private fun View.applyActionConstraint() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this@MeryAppBar)

        constraintSet.constrainWidth(this.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(this.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(
            this.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            this.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            this.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(this@MeryAppBar)
    }

    companion object {

        private val APP_BAR_HEIGHT = 56.dp
        private val APP_BAR_HORIZONTAL_PADDING = 16.dp
        private val NAVIGATION_ICON_BUTTON_SIZE = 22.dp
        private val BUTTON_HORIZONTAL_PADDING = 4.dp
        private val BUTTON_VERTICAL_PADDING = 5.dp
    }
}

@BindingAdapter("app:isActionEnabled")
fun MeryAppBar.setActionEnabled(enabled: Boolean) {
    isActionEnabled = enabled
}

@BindingAdapter("app:onNavigationClick")
fun MeryAppBar.setOnNavigationClick(onClickListener: OnClickListener) {
    onNavigationClickListener = onClickListener
}

@BindingAdapter("app:onActionClick")
fun MeryAppBar.setOnActionClick(onClickListener: OnClickListener) {
    onActionClickListener = onClickListener
}

@BindingAdapter("app:navigationIcon")
fun MeryAppBar.setNavigationIcon(icon: Drawable?) {
    navigationIcon = icon
}

@BindingAdapter("app:navigationText")
fun MeryAppBar.setNavigationText(text: CharSequence?) {
    navigationText = text
}

@BindingAdapter("app:title")
fun MeryAppBar.setTitle(text: CharSequence) {
    title = text
}

@BindingAdapter("app:action")
fun MeryAppBar.setActionText(text: CharSequence) {
    actionText = text
}
