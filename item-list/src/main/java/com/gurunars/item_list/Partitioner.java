package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.List;

class Partitioner<ItemType> {

    static class Partition<ItemType> {
        private List<ItemType> head = new ArrayList<>();
        private List<ItemType> middle = new ArrayList<>();
        private List<ItemType> tail = new ArrayList<>();

        Partition(List<ItemType> list, int startOffset, int endOffset) {
            int sourceLastIndex = list.size() - endOffset;
            if (sourceLastIndex < startOffset) {
                head = new ArrayList<>(list);
            } else {
                head = new ArrayList<>(list.subList(0, startOffset));
                middle = new ArrayList<>(list.subList(startOffset, sourceLastIndex));
                tail = new ArrayList<>(list.subList(sourceLastIndex, list.size()));
            }
        }

        List<ItemType> getHead() {
            return head;
        }

        List<ItemType> getMiddle() {
            return middle;
        }

        List<ItemType> getTail() {
            return tail;
        }

    }

    static class PartitionTuple<ItemType> {

        private Partition<ItemType> source;
        private Partition<ItemType> target;
        private int startOffset;
        private int endOffset;

        Partition<ItemType> getSource() {
            return source;
        }

        Partition<ItemType> getTarget() {
            return target;
        }

        int getStartOffset() {
            return startOffset;
        }

        int getEndOffset() {
            return endOffset;
        }
    }

    int getStartOffset(List<ItemType> source, List<ItemType> target) {
        int sizeLimit = Math.min(source.size(), target.size());
        int startOffset = 0;
        for (int i=0; i < sizeLimit; i ++) {
            if (!source.get(i).equals(target.get(i))) {
                break;
            }
            startOffset = i;
        }
        return startOffset;
    }

    int getEndOffset(List<ItemType> source, List<ItemType> target) {
        int sizeLimit = Math.min(source.size(), target.size());
        int endOffset = 0;
        int sourceLast = source.size() - 1;
        int targetLast = target.size() - 1;
        for (int i=0; i < sizeLimit; i ++) {
            if (!source.get(sourceLast-i).equals(target.get(targetLast-i))) {
                break;
            }
            endOffset = i;
        }
        return endOffset;
    }

    PartitionTuple<ItemType> apply(List<ItemType> source, List<ItemType> target) {
        int startOffset = getStartOffset(source, target);
        int endOffset = getEndOffset(source, target);

        PartitionTuple<ItemType> tuple = new PartitionTuple<>();
        tuple.source = new Partition<>(source, startOffset, endOffset);
        tuple.target = new Partition<>(target, startOffset, endOffset);
        tuple.startOffset = startOffset;
        tuple.endOffset = endOffset;
        return tuple;
    }

}
