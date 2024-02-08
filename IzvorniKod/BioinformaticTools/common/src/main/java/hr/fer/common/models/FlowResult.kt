package hr.fer.common.models

sealed class FlowResult {
	class Error(var message: String? = ""): FlowResult()
	object Success : FlowResult()
}