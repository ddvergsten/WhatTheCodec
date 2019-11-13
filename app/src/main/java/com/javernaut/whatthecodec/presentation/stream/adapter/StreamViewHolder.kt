package com.javernaut.whatthecodec.presentation.stream.adapter

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.javernaut.whatthecodec.R
import com.javernaut.whatthecodec.presentation.stream.adapter.animator.HeightAnimator
import com.javernaut.whatthecodec.presentation.stream.model.Stream
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_stream.view.*

class StreamViewHolder(override val containerView: View, listener: OnExpandStatusChangeListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private lateinit var item: Stream

    private val subAdapter = StreamFeaturesAdapter()

    private val subListHeightAnimator = HeightAnimator(containerView.streamFeatures)

    init {
        val layoutManager = GridLayoutManager(containerView.context, 2)
        containerView.streamFeatures.layoutManager = layoutManager
        containerView.streamFeatures.adapter = subAdapter

        containerView.expandToggle.setOnClickListener {
            item.isExpanded = !item.isExpanded
            listener.onExpandStatusChange(this, item.isExpanded)
        }
    }

    fun bindTo(stream: Stream) {
        this.item = stream

        containerView.streamTitle.apply {
            setText(R.string.page_stream_title_prefix)
            append(stream.index.toString())
            if (stream.title != null) {
                append(" - " + stream.title)
            }
        }

        subAdapter.items = stream.features

        subListHeightAnimator.setExpanded(stream.isExpanded)

        containerView.streamFeatures.alpha = if (stream.isExpanded) 1f else 0f
    }

    fun animateList(isExpanded: Boolean) {
        containerView.streamFeatures
                .animate()
                .alpha(if (isExpanded) 1f else 0f)

        subListHeightAnimator.animateExpandedTo(isExpanded)
    }

    interface OnExpandStatusChangeListener {
        fun onExpandStatusChange(viewHolder: StreamViewHolder, isExpanded: Boolean)
    }
}