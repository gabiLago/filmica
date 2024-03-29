package es.lagoblasco.filmica.view.util

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.Toast
import es.lagoblasco.filmica.R
import es.lagoblasco.filmica.data.FilmsRepo

abstract class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView

        setColor(recyclerView, itemView, dX, c)

        setIcon(recyclerView, itemView, c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    }

    private fun setIcon(
        recyclerView: RecyclerView,
        itemView: View,
        c: Canvas
    ) {
        val checkIcon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_check)!!

        val iconMargin = (itemView.height - checkIcon.intrinsicHeight) / 3
        val iconTop = itemView.top + (itemView.height - checkIcon.intrinsicHeight) / 2
        val iconRight = itemView.left + iconMargin + checkIcon.intrinsicWidth
        val iconLeft = itemView.left + iconMargin
        val iconBottom = iconTop + checkIcon.intrinsicHeight

        checkIcon.setBounds(
            iconLeft,
            iconTop,
            iconRight,
            iconBottom
        )

        checkIcon.draw(c)
    }

    private fun setColor(
        recyclerView: RecyclerView,
        itemView: View,
        dX: Float,
        c: Canvas
    ) {
        val color = ContextCompat.getColor(recyclerView.context, R.color.colorPrimaryDark)
        val background = ColorDrawable(color)
        background.setBounds(
            itemView.left,
            itemView.top,
            itemView.left + dX.toInt(),
            itemView.bottom
        )

        background.draw(c)
    }

}