package com.sun.mygif.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import com.sun.mygif.R
import com.sun.mygif.service.GifHeadService
import com.sun.mygif.ui.base.BaseActivity
import com.sun.mygif.ui.base.OnFragmentInteractionListener
import com.sun.mygif.ui.detail.DetailFragment
import com.sun.mygif.ui.explore.ExploreContentFragment
import com.sun.mygif.ui.favorite.FavoriteContentFragment
import com.sun.mygif.ui.home.HomeContentFragment
import com.sun.mygif.utils.EXTRA_GIF_ID
import com.sun.mygif.utils.ScreenHelper.Companion.backOutDetailScreen
import com.sun.mygif.utils.ScreenHelper.Companion.backToFirstScreen
import com.sun.mygif.utils.ScreenHelper.Companion.isShowingDetailScreen
import com.sun.mygif.utils.ScreenHelper.Companion.isShowingFirstScreen
import com.sun.mygif.utils.ScreenHelper.Companion.isShowingSearchScreen
import com.sun.mygif.utils.isFirstScreen
import kotlinx.android.synthetic.main.activity_main.*

private const val PERMISSION_REQUEST_CODE = 201

class MainActivity : BaseActivity(), OnFragmentInteractionListener {

    override val layoutResource: Int = R.layout.activity_main

    override fun initComponent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setupPermission()
        }
        openFragment(R.id.frameContent, HomeContentFragment(), false)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> openFragment(R.id.frameContent, HomeContentFragment(), false)
                R.id.item_explore -> openFragment(R.id.frameContent, ExploreContentFragment(), false)
                R.id.item_favorite -> openFragment(R.id.frameContent, FavoriteContentFragment(), false)
            }
            true
        }
    }

    override fun initData() {
        getPreferences(Context.MODE_PRIVATE).run {
            getString(EXTRA_GIF_ID, null)?.let { id ->
                openFragment(R.id.constraintMain, DetailFragment.newInstance(id), true)
                edit().remove(EXTRA_GIF_ID).apply()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupPermission() {
        if (!Settings.canDrawOverlays(this))
            startActivityForResult(getSettingIntent(this), PERMISSION_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                showWarningPermissionDialog()
            }
        }
    }

    override fun onTabChange(itemId: Int, fragment: Fragment) {
        backToFirstScreen(this)
        if (fragment.isFirstScreen()) {
            bottomNavigation?.selectedItemId = itemId
            openFragment(R.id.frameContent, fragment, false)
        }
    }

    override fun onSaveState(gifId: String) {
        getPreferences(Context.MODE_PRIVATE).edit().putString(EXTRA_GIF_ID, gifId).apply()
    }

    override fun onBackPressed() {
        when {
            isShowingDetailScreen(this, R.id.constraintMain) -> backOutDetailScreen(this, R.id.constraintMain)
            isShowingSearchScreen(this, R.id.constraintMain) -> backToFirstScreen(this)
            isShowingFirstScreen(this, R.id.frameContent) -> showAppExitDialog()
            else -> super.onBackPressed()
        }
    }

    private fun showAppExitDialog() = with(AlertDialog.Builder(this)) {
        setIcon(R.drawable.ic_exit_to_app_red_24dp)
        setTitle(getString(com.sun.mygif.R.string.title_app_closing_dialog))
        setMessage(getString(com.sun.mygif.R.string.notification_app_closing_dialog))

        setPositiveButton(context.getString(com.sun.mygif.R.string.title_yes)) { _, _ ->
            finish()
        }
        setNegativeButton(context.getString(com.sun.mygif.R.string.title_no)) { _, _ ->
        }
        show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showWarningPermissionDialog() = with(AlertDialog.Builder(this)) {
        setIcon(R.drawable.ic_warning_24dp)
        setTitle(getString(R.string.title_warning))
        setMessage(getString(R.string.message_warning_permission))

        setPositiveButton(context.getString(R.string.title_yes)) { _, _ ->
            startActivityForResult(getSettingIntent(this@MainActivity), PERMISSION_REQUEST_CODE)
        }
        setNegativeButton(context.getString(R.string.title_no)) { _, _ ->
            finish()
        }
        show()
    }

    override fun onResume() {
        super.onResume()
        if (GifHeadService.isRunning(this)) stopService(GifHeadService.getIntent(this, null))
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.M)
        @JvmStatic
        private fun getSettingIntent(context: Context) = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package: ${context.packageName}")
        )

        @JvmStatic
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
