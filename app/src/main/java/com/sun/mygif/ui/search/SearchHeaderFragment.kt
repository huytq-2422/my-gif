package com.sun.mygif.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.sun.mygif.R
import com.sun.mygif.ui.base.BaseFragment
import com.sun.mygif.utils.EXTRA_SEARCH_KEY
import kotlinx.android.synthetic.main.fragment_search_header.*

private const val DELAY_BETWEEN_TOUCHES = 150L
private const val EMPTY_STRING = ""

class SearchHeaderFragment : BaseFragment(), View.OnClickListener {
    override val layoutResource = R.layout.fragment_search_header

    private var searchKey: String? = null

    override fun initComponents() {
        handleEventClick()
        handleInputChange()
    }

    private fun handleEventClick() {
        buttonSearch.setOnClickListener(this)
        buttonCancel.setOnClickListener(this)
        constraintTitleBox.setOnClickListener(this)
    }

    override fun initData() {
        searchKey = arguments?.getString(EXTRA_SEARCH_KEY)
        searchKey?.let {
            search(keyword = it)
        } ?: showInputBox()
    }

    private fun search(keyword: String) {
        showTitleBox(keyword)
        addFragment(R.id.frameSearchContent, SearchContentFragment.newInstance(keyword), true)
    }

    private fun showTitleBox(keyword: String) {
        constraintTitleBox.visibility = View.VISIBLE
        titleSearch.text = keyword
        inputSearch.setText(EMPTY_STRING)
        hideSoftKeyboard()
    }

    private fun showInputBox() {
        constraintTitleBox.visibility = View.GONE
        showSoftKeyboard()
    }

    private fun handleInputChange() = with(inputSearch) {
        setOnEditorActionListener { _, actionId, _ ->
            (actionId == EditorInfo.IME_ACTION_SEARCH).also {
                if (it) inputSearch.text.toString().also { key -> search(keyword = key) }
            }
        }

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (text.isEmpty()) buttonCancel.setImageResource(R.drawable.ic_arrow_back_black_24dp)
                else if (text.length == 1) buttonCancel.setImageResource(R.drawable.ic_close_black_24dp)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonSearch -> inputSearch.text.toString().also { search(keyword = it) }
            R.id.buttonCancel -> stopSearching()
            R.id.constraintTitleBox -> startSearchingAnother()
        }
    }

    private fun stopSearching() {
        if (inputSearch.text.isEmpty()) activity?.onBackPressed()
        inputSearch.setText(EMPTY_STRING)
        hideSoftKeyboard()
    }

    private fun startSearchingAnother() {
        if (isShowingSearchedResults()) hideSearchedResults()
        showInputBox()
    }

    private fun showSoftKeyboard() = with(inputSearch) {
        Handler().postDelayed({
            dispatchTouch(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN)
            dispatchTouch(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP)
        }, DELAY_BETWEEN_TOUCHES)
    }

    private fun hideSoftKeyboard() = with(inputSearch) {
        clearFocus()
        (context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(windowToken, 0)
        }
    }

    private fun isShowingSearchedResults() =
        activity?.supportFragmentManager?.findFragmentById(R.id.frameSearchContent) is SearchContentFragment

    private fun hideSearchedResults() = activity?.supportFragmentManager?.popBackStack()

    private fun View.dispatchTouch(downTime: Long, eventTime: Long, motionEvent: Int) =
        dispatchTouchEvent(MotionEvent.obtain(downTime, eventTime, motionEvent, 0f, 0f, 0))

    companion object {
        @JvmStatic
        fun newInstance(searchKey: String?) = SearchHeaderFragment().apply {
            searchKey?.let { arguments = Bundle().apply { putString(EXTRA_SEARCH_KEY, it) } }
        }
    }
}
