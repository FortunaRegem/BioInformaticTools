package hr.fer.common.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.fer.common.models.BindableItem

class DataBindingRecyclerAdapter(
	private var lifecycleOwner: LifecycleOwner?,
) : ListAdapter<BindableItem, BindingViewHolder>(DiffCallback())  {

	override fun getItemViewType(position: Int): Int {
		return getItem(position).layoutId
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): BindingViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding: ViewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
			.apply {
				lifecycleOwner = this@DataBindingRecyclerAdapter.lifecycleOwner
			}
		return BindingViewHolder(binding)
	}

	override fun onBindViewHolder(
		holder: BindingViewHolder,
		position: Int
	) {
		holder.run {
			val item = getItem(position)
			item.bind(binding)
			if (binding.hasPendingBindings()) {
				binding.executePendingBindings()
			}
		}
	}

}

class BindingViewHolder(
	val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root)


private class DiffCallback : DiffUtil.ItemCallback<BindableItem>() {
	override fun areItemsTheSame(
		oldItem: BindableItem,
		newItem: BindableItem
	): Boolean {
		val oldData = oldItem.data
		val newData = newItem.data
		return if (oldData is BindableItemComparator
			&& newData is BindableItemComparator
		) {
			oldData.isSameItem(newData)
		} else oldData == newData
	}

	@SuppressLint("DiffUtilEquals")
	override fun areContentsTheSame(
		oldItem: BindableItem,
		newItem: BindableItem
	): Boolean {
		val oldViewModel = oldItem.data
		val newViewModel = newItem.data
		return if (oldViewModel is BindableItemComparator
			&& newViewModel is BindableItemComparator
		) {
			oldViewModel.isSameContent(newViewModel)
		} else oldViewModel == newViewModel
	}
}

interface BindableItemComparator {
	fun isSameItem(other: Any): Boolean
	fun isSameContent(other: Any): Boolean
}

private fun BindableItem.bind(binding: ViewDataBinding) {
	val isVariableFound = binding.setVariable(variableId, data)
	if (isVariableFound.not()) {
		throw IllegalStateException(
			buildErrorMessage(variableId, binding)
		)
	}
}

private fun buildErrorMessage(
	variableId: Int,
	binding: ViewDataBinding
): String {
	val variableName = DataBindingUtil.convertBrIdToString(variableId)
	val className = binding::class.simpleName
	return "Failed to find variable='$variableName' in the following databinding layout: $className"
}