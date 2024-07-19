package ksc.campus.tech.kakao.map

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ksc.campus.tech.kakao.map.views.KakaoMapFragment
import ksc.campus.tech.kakao.map.views.MainActivity
import ksc.campus.tech.kakao.map.views.SearchResultFragment
import org.junit.Rule
import org.junit.Test



class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mapViewShownOnStart(){
        activityRule.scenario.onActivity {
            assert(checkHasFragmentOfType<KakaoMapFragment>(it.supportFragmentManager.fragments))
            assert(!checkHasFragmentOfType<SearchResultFragment>(it.supportFragmentManager.fragments))
        }
    }

    @Test
    fun searchListShownOnSearchBarClick(){
        onView(withId(R.id.input_search))
            .perform(click())

        activityRule.scenario.onActivity {
            assert(!checkHasFragmentOfType<KakaoMapFragment>(it.supportFragmentManager.fragments))
            assert(checkHasFragmentOfType<SearchResultFragment>(it.supportFragmentManager.fragments))
        }
    }



    private inline fun <reified F:Fragment> checkHasFragmentOfType(list:List<Fragment>):Boolean{
        return list.find {
            it::class.java == F::class.java
        } != null
    }
}