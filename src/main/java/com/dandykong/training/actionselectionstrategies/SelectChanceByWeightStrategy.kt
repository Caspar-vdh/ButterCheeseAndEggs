package com.dandykong.training.actionselectionstrategies

import com.dandykong.butter.exception.ButterException
import kotlin.random.Random

@Suppress("OPT_IN_USAGE", "OPT_IN_OVERRIDE")
class SelectChanceByWeightStrategy: ActionSelectionStrategy {
    override fun selectAction(weights: UByteArray): Int {
        val weightIndices:MutableList<Pair<UByte, Int>> = mutableListOf()
        for (i in weights.indices) {
            if (weights[i] != 0.toUByte()) {
                weightIndices.add(Pair(weights[i], i))
            }
        }
        if (weightIndices.isEmpty()) {
            throw ButterException("No actions with non-zero weight")
        }
        if (weightIndices.size == 1) {
            return weightIndices[0].second
        }
        weightIndices.sortByDescending { it.first }
        val rand = Random.nextInt(weightIndices.sumOf { it.first.toInt() })
        var sum = 0
        for (weightIndex in weightIndices) {
            sum += weightIndex.first.toInt()
            if (rand < sum) return weightIndex.second
        }
        return weightIndices.last().second
    }
}
