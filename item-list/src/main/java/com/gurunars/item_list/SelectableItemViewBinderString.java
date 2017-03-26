package com.gurunars.item_list;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

class SelectableItemViewBinderString<ItemType extends Item> extends ItemViewBinderString<SelectableItem<ItemType>> {

    @Override
    protected boolean shouldAnimate(SelectableItem<ItemType> item, SelectableItem<ItemType> previousItem) {
        return super.shouldAnimate(item, previousItem) &&
                ! item.getItem().payloadsEqual(previousItem.getItem());
    }

    @Override
    public void bind(TextView itemView, SelectableItem<ItemType> item, @Nullable SelectableItem<ItemType> previousItem) {
        super.bind(itemView, item, previousItem);
        itemView.setBackgroundColor(item.isSelected() ? Color.RED : Color.TRANSPARENT);
    }

}
