package com.gurunars.item_list.selectable_example

import com.gurunars.item_list.Item

internal class AnimalItem(private var id: Long, private var version: Int, private var type: AnimalItem.Type) : Item {

    override fun getType(): Enum<*> {
        return type
    }

    override fun getId(): Long {
        return id
    }

    override fun payloadsEqual(other: Item): Boolean {
        return other is AnimalItem && version == other.version
    }

    internal enum class Type {
        MONKEY, TIGER, WOLF, LION
    }

    fun update() {
        this.version++
    }

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }

}