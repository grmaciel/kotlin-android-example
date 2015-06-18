package jp.satorufujiwara.kotlin.ui.main.drawer

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.bindView
import com.bumptech.glide.Glide
import jp.satorufujiwara.binder.Section
import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter
import jp.satorufujiwara.kotlin.AbstractFragment
import jp.satorufujiwara.kotlin.R
import jp.satorufujiwara.kotlin.data.inflate
import kotlin.platform.platformStatic
import kotlin.properties.Delegates

public class MainDrawerFragment : AbstractFragment() {

    companion object {
        platformStatic fun newInstance(): MainDrawerFragment {
            return MainDrawerFragment()
        }
    }

    val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    val headerImage: ImageView by bindView(R.id.headerImage)
    val headerLayout: View by bindView(R.id.headerLayout)
    val adapter: RecyclerBinderAdapter<MainDrawerSection, MainDrawerViewType> = RecyclerBinderAdapter()
    val scrollListener: OnScrollListener by Delegates.lazy {
        OnScrollListener(headerLayout)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflate(R.layout.main_drawer_fragment, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
        recyclerView.addOnScrollListener(scrollListener)

        adapter.add(MainDrawerSection.HEADER, MainDrawerTransparentBinder(getActivity()))
        adapter.add(MainDrawerSection.NAVIGATION, MainDrawerNavigationBinder(getActivity(),
                getString(R.string.main_drawer_navigation_home)))
        for (i in 0..10) {
            adapter.add(MainDrawerSection.NAVIGATION, MainDrawerNavigationBinder(getActivity(),
                    "Menu : " + i))
        }
        Glide.with(this).load("https://raw.githubusercontent.com/satorufujiwara/kotlin-android-example/master/art/header_image.jpg").into(headerImage)
    }

    override fun onDestroyView() {
        recyclerView.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }

    enum class MainDrawerSection : Section {
        HEADER,
        NAVIGATION
    }

    class OnScrollListener(val headerLayout: View) : RecyclerView.OnScrollListener() {
        var y: Int = 0
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            y += dy
            if (y > headerLayout.getHeight()) {
                return
            }
            headerLayout.setTranslationY((-y / 2).toFloat())
        }
    }
}