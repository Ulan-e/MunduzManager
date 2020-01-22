package com.ulan.munduz.manager.ui.products

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulan.app.munduz.helpers.Constants.Companion.PRODUCTS_DATA
import com.ulan.app.munduz.ui.Product
import com.ulan.munduz.manager.R
import com.ulan.munduz.manager.adapter.ProductAdapter
import com.ulan.munduz.manager.adapter.listeners.OnItemClickListener
import com.ulan.munduz.manager.data.repository.Repository
import com.ulan.munduz.manager.data.repository.RepositoryImpl
import com.ulan.munduz.manager.ui.detail.DetailsActivity
import kotlinx.android.synthetic.main.products_activity.*

class ProductsActivity : AppCompatActivity(),
    OnItemClickListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener,
    ProductsView {

    private lateinit var mPresenter: ProductsPresenter
    private lateinit var mRepository: Repository
    private lateinit var mProducts: MutableList<Product>
    private lateinit var mAdapter: ProductAdapter
    private lateinit var searchView : SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products_activity)
        mRepository = RepositoryImpl(this)
        mPresenter =
            ProductsPresenterImpl(
                mRepository
            )
        mPresenter.attachView(this)
        mPresenter.loadProducts()
        mPresenter.setToolbarTitle()
    }

    override fun setToolbar(title: String) {
        setSupportActionBar(products_toolbar)
        supportActionBar?.title = title
        products_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        products_toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun showProducts(products: ArrayList<Product>) {
        mProducts = products
        val layoutManager = LinearLayoutManager(this)
        products_recycler_view.layoutManager = layoutManager
        products_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mAdapter = ProductAdapter(
            this, products,
            this@ProductsActivity
        )
        products_recycler_view.adapter = mAdapter
    }

    override fun showNoProducts(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClick(product: Product?) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(PRODUCTS_DATA, product)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.manager_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.search)
        searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = "Search"
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        mAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapter.filter.filter(newText)
        return true
    }

    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        return true
    }

}