package com.dandykong.training.actionselectionstrategies

import kotlin.random.Random

class MultipleSelectionStrategy(private vararg val strategies: Pair<ActionSelectionStrategy, Int>):
    ActionSelectionStrategy {

    private val totalChance = strategies.sumOf { pair -> pair.second }

    @ExperimentalUnsignedTypes
    override fun selectAction(weights: UByteArray): Int {
        val chance = Random.nextInt(totalChance);
        var sum = 0

        for (strategy in strategies){
            sum += strategy.second
            if (chance < sum) {
                return strategy.first.selectAction(weights)
            }
        }
        // fallback, shouldn't happen
        return strategies[0].first.selectAction(weights)
    }
}
