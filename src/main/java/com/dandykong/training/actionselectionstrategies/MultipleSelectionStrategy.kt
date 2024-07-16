package com.dandykong.training.actionselectionstrategies

import com.dandykong.training.basics.ActionSelectionStrategy

class DualSelectionStrategy(val strategies: List<Pair<ActionSelectionStrategy, Int>>): ActionSelectionStrategy {

    val totalChance = strategies.stream().map { pair -> pair.second }.

    override fun selectAction(weights: UByteArray): Int {
        return 3;
    }
}