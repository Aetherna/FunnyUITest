package dev.aetherna.funnyuitest

import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.picasso.transformations.BlurTransformation


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image: ImageView = findViewById(R.id.toTransition)
        val toolbar: CollapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
        val root: CoordinatorLayout = findViewById(R.id.root)
        val appBar: AppBarLayout = findViewById(R.id.appBar)
        val footer: View = findViewById(R.id.footer)

        val rec: NestedScrollView = findViewById(R.id.rec)

        var lol = false

        Picasso.with(this)
            .load("https://vignette.wikia.nocookie.net/uncyclopedia/images/0/0b/Rabbit.jpg")
            .fit()
            .centerInside()
            .into(image)

        footer.setOnClickListener {

            rec.fullScroll(View.FOCUS_UP)
            appBar.setExpanded(true, true)

            Single.just("https://vignette.wikia.nocookie.net/uncyclopedia/images/0/0b/Rabbit.jpg")
                .subscribeOn(Schedulers.io())
                .map {
                    Picasso.with(this)
                        .load(it)
                        .apply { if (lol) transform(BlurTransformation(image.context, 80)) }
                        .get()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    image.setImageBitmap(it)
                }
                .doOnSuccess { animate(image, lol) }
                .subscribe()

            lol = !lol
        }

        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 -> doThey(toolbar, footer) })


    }

    private fun doThey(image: View, footer: View) {
        val firstPosition = IntArray(2)
        val secondPosition = IntArray(2)

        image.getLocationOnScreen(firstPosition)
        footer.getLocationOnScreen(secondPosition)

        // Rect constructor parameters: left, top, right, bottom
        val rectFirstView = Rect(
            firstPosition[0], firstPosition[1],
            firstPosition[0] + image.measuredWidth, firstPosition[1] + image.measuredHeight
        )
        val rectSecondView = Rect(
            secondPosition[0],
            secondPosition[1],
            secondPosition[0] + footer.measuredWidth,
            secondPosition[1] + footer.measuredHeight
        )
        if (rectFirstView.intersect(rectSecondView))
            footer.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        else
            footer.setBackgroundColor(resources.getColor(R.color.colorAccent))

    }

    private fun animate(image: ImageView, lol: Boolean) {
        val from = resources.getDimension(R.dimen.header_size_normal).toInt()
        val to = resources.getDimension(R.dimen.header_size_large).toInt()

        val anim = if (lol) ValueAnimator.ofInt(from, to) else ValueAnimator.ofInt(to, from)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = image.layoutParams
            layoutParams.height = `val`
            image.layoutParams = layoutParams
        }
        anim.start()
    }
}
