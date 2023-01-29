package com.davidread.starwarsdatabase.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.davidread.starwarsdatabase.R
import com.davidread.starwarsdatabase.databinding.ActivityCategoryBinding

/**
 * Activity representing the top-level category fragment destinations of the app. Controls the
 * navigation drawer and appropriate switching of category fragments.
 */
class CategoryActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    /**
     * Binding object for this activity's layout.
     */
    private val binding: ActivityCategoryBinding by lazy {
        ActivityCategoryBinding.inflate(layoutInflater)
    }

    /**
     * Manages which fragments are put in [R.id.nav_host_fragment].
     */
    private val navController: NavController by lazy {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    /**
     * Set of id resources that represent top-level fragment destinations in the nav controller.
     */
    private val topLevelDestinations: Set<Int> = setOf(
        R.id.person_names_fragment,
        R.id.films_list_fragment,
        R.id.starships_list_fragment,
        R.id.vehicles_list_fragment,
        R.id.species_list_fragment,
        R.id.planets_list_fragment
    )

    /**
     * Manages the behavior of the navigation drawer button in the action bar.
     */
    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(topLevelDestinations, binding.drawerLayout)
    }

    /**
     * Animates the navigation drawer button when the drawer is opened/closed.
     */
    private val drawerToggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.drawer_open_action_label,
            R.string.drawer_close_action_label
        )
    }

    /**
     * Invoked when this activity is initially created. It sets up the [navController] and
     * navigation drawer objects.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.apply {
            navigationView.setupWithNavController(navController)
            drawerLayout.addDrawerListener(drawerToggle)
        }
        navController.addOnDestinationChangedListener(this)

        /* Set initial checked item for navigation drawer with this condition because nav controller
         * current destination is preserved through configuration changes. */
        if (navController.currentDestination?.id == R.id.person_names_fragment) {
            binding.navigationView.setCheckedItem(R.id.person_names_fragment)
        }
    }

    /**
     * Invoked when an item in the action bar is selected. It passes this event to [drawerToggle]
     * to handle first. If not handled, then the superclass handles it.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    /**
     * Invoked when the up button in the action bar is selected. It passes this event to
     * [navController] to handle first. If not handled, then the superclass handles it.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Invoked when the [navController] shows a new fragment. It determines whether to show the
     * drawer toggle in the action bar and whether to sync the drawer toggle's state with it's
     * drawer layout.
     */
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (topLevelDestinations.contains(destination.id)) {
            drawerToggle.isDrawerIndicatorEnabled = true
            drawerToggle.syncState()
        } else {
            drawerToggle.isDrawerIndicatorEnabled = false
        }
    }
}