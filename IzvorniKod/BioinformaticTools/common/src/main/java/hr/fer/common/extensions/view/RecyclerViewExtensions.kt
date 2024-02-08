package hr.fer.common.extensions.view

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.fer.common.adapters.DataBindingRecyclerAdapter
import hr.fer.common.models.BindableItem

@BindingAdapter("items", "owner", requireAll = false)
fun RecyclerView.setRecyclerViewItems(
	items: List<BindableItem>?,
	lifecycleOwner: LifecycleOwner?
) {
	var adapter = (this.adapter as? DataBindingRecyclerAdapter)
	if (adapter == null) {
		adapter = DataBindingRecyclerAdapter(lifecycleOwner = lifecycleOwner)
		this.adapter = adapter
	}

	adapter.submitList(items)
	this.startLayoutAnimation()
}

fun RecyclerView.getCurrentRecyclerViewPosition() =
	(this.layoutManager as LinearLayoutManager)
		.findFirstVisibleItemPosition()