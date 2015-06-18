package jp.satorufujiwara.kotlin.ui.main

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import jp.satorufujiwara.binder.Section
import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter
import jp.satorufujiwara.kotlin.AbstractFragment
import jp.satorufujiwara.kotlin.R
import jp.satorufujiwara.kotlin.data.api.dto.Repo
import jp.satorufujiwara.kotlin.data.inflate
import jp.satorufujiwara.kotlin.ui.main.drawer.MainRepoBinder
import rx.Observable
import timber.log.Timber
import javax.inject.Inject
import kotlin.platform.platformStatic

public class MainFragment : AbstractFragment() {

    companion object {
        platformStatic fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    val adapter: RecyclerBinderAdapter<MainSection, MainViewType> = RecyclerBinderAdapter()
    var githubObservable: Observable<List<Repo>>? = null
        @Inject set

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        MainComponent.Initializer.init(activity as MainActivity).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflate(R.layout.main_fragment, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
    }

    override fun onResume() {
        super.onResume()
        githubObservable?.subscribe {
            Timber.d("repos = %s", it)
            adapter.clear();
            it.forEach { adapter.add(MainSection.CONTENTS, MainRepoBinder(getActivity(), it)) }
            adapter.notifyDataSetChanged()
        }
    }

    enum class MainSection : Section {
        CONTENTS
    }
}