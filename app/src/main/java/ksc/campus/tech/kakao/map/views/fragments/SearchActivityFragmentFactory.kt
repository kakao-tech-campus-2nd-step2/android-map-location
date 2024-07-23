package ksc.campus.tech.kakao.map.views.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import ksc.campus.tech.kakao.map.view_models.SearchActivityViewModel

class SearchActivityFragmentFactory(private val viewModel: SearchActivityViewModel) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            KakaoMapFragment::class.java.name->{
                KakaoMapFragment(viewModel)
            }
            SearchResultFragment::class.java.name->{
                SearchResultFragment(viewModel)
            }
            else->{
                super.instantiate(classLoader, className)
            }
        }
    }
}