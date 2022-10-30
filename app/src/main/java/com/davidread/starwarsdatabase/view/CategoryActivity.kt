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
     * Allows easy access to view objects from the layout.
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
     * Manages the behavior of the navigation drawer button in the action bar.
     */
    private val appBarConfiguration: AppBarConfiguration by lazy {
        val topLevelDestinations = setOf(
            R.id.people_list_fragment,
            R.id.films_list_fragment,
            R.id.starships_list_fragment,
            R.id.vehicles_list_fragment,
            R.id.species_list_fragment,
            R.id.planets_list_fragment
        )
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
        if (navController.currentDestination?.id == R.id.people_list_fragment) {
            binding.navigationView.setCheckedItem(R.id.people_list_fragment)
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
     * Invoked when the [navController] shows a new fragment. It calls this because [drawerToggle]
     * is not notified to update the navigation drawer button when a navigation drawer option is
     * selected.
     */
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        drawerToggle.syncState()
    }
}