package com.example.primenumber.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.lang.Math.floor

class ViewModel : ViewModel() {
    var primeNumbers: MutableList<Int> = mutableListOf()

    protected val _loading: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 2)
    val loading: SharedFlow<Boolean> = _loading

    fun isPrimeNum(currentNum: Int): Boolean {
        if (currentNum < 2) {
            return false
        }
        for (i in 2..currentNum / 2) {
            if (currentNum % i == 0) {
                return false
            }
        }
        return true
    }

    suspend fun allPrimeNum(start: Int, end: Int) {
        val allNum: MutableList<Int> = mutableListOf()

        _loading.emit(true)

        for (i in start..end) {
            allNum.add(i)
        }

        val diff = end - start
        val totalCoroutine = floor(diff.toDouble() / 1000).toInt()

        if (diff < 1000) {
            viewModelScope.launch {
                async {
                    for (i in 0..allNum.size - 1) {
                        if (isPrimeNum(allNum[i])) {
                            primeNumbers.add(allNum[i])
                        }
                    }
                }.await()
            }
        } else {
            viewModelScope.launch {
                (1..totalCoroutine).map {
                    for (i in (it * 1000 - 1000)..(it * 1000)) {
                        async {
                            if (isPrimeNum(allNum[i])) {
                                primeNumbers.add(allNum[i])
                            }
                        }.await()
                    }
                }
            }
            viewModelScope.launch {
                async {
                    for (i in totalCoroutine * 1000..allNum.size - 1) {
                        if (isPrimeNum(allNum[i])) {
                            primeNumbers.add(allNum[i])
                        }
                    }
                }.await()
            }
        }

        delay(2000)
        _loading.emit(false)
    }

}