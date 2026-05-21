package com.rahulghag.splittrip.test

import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor() :
    SplitTripViewModel<CounterState, CounterIntent, CounterEvent>(
        initialState = CounterState()
    ) {

    override fun onIntent(intent: CounterIntent) {
        when (intent) {
            CounterIntent.Increment -> updateState {
                copy(count = count + 1)
            }
            CounterIntent.Decrement -> updateState {
                copy(count = count - 1)
            }
            CounterIntent.Reset -> {
                updateState { copy(count = 0) }
                sendEvent(CounterEvent.ShowResetMessage)
            }
        }
    }
}
