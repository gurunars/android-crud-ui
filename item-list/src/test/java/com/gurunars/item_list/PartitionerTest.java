package com.gurunars.item_list;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PartitionerTest {

    private Partitioner<Integer> optimizer = new Partitioner<>();

    private void checkOffsets(int expectedStartOffset, int expectedEndOffset,
                              List<Integer> one, List<Integer> two) {
        assertEquals(expectedStartOffset, optimizer.getStartOffset(one, two));
        assertEquals(expectedEndOffset, optimizer.getEndOffset(one, two));
    }

    @Test
    public void getOffsetsWithRemoval() throws Exception {
        checkOffsets(6, 2,
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12),
                Arrays.asList(1,2,3,4,5,6,7,10,11,12));
    }

    @Test
    public void getOffsetsWithReplacement() throws Exception {
        checkOffsets(6, 2,
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12),
                Arrays.asList(1,2,3,4,5,6,7,14,16,10,11,12));
    }

    @Test
    public void getOffsetsWithPrepend() throws Exception {
        checkOffsets(0, 11,
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12),
                Arrays.asList(16,17,1,2,3,4,5,6,7,8,9,10,11,12));
    }

    @Test
    public void getOffsetsWithAppend() throws Exception {
        checkOffsets(11, 0,
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12),
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,16,17));
    }

    @Test
    public void getOffsetsWithPrependAndRemove() throws Exception {
        checkOffsets(0, 9,
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12),
                Arrays.asList(16,17,3,4,5,6,7,8,9,10,11,12));
    }

    @Test
    public void getOffsetsWithAppendAndRemove() throws Exception {
        checkOffsets(9, 0,
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12),
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,16,17));
    }

    @Test
    public void getPartition() throws Exception {
        Partitioner.Partition<Integer> partition = new Partitioner.Partition<>(
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12), 4, 4
        );

        assertEquals(Arrays.asList(1,2,3,4), partition.getHead());
        assertEquals(Arrays.asList(5,6,7,8), partition.getMiddle());
        assertEquals(Arrays.asList(9,10,11,12), partition.getTail());
    }

    @Test
    public void getPartitionEmptyTail() throws Exception {
        Partitioner.Partition<Integer> partition = new Partitioner.Partition<>(
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12), 4, 0
        );

        assertEquals(Arrays.asList(1,2,3,4), partition.getHead());
        assertEquals(Arrays.asList(5,6,7,8,9,10,11,12), partition.getMiddle());
        assertEquals(new ArrayList<Integer>(), partition.getTail());
    }

    @Test
    public void getPartitionEmptyHead() throws Exception {
        Partitioner.Partition<Integer> partition = new Partitioner.Partition<>(
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12), 0, 4
        );

        assertEquals(new ArrayList<Integer>(), partition.getHead());
        assertEquals(Arrays.asList(1,2,3,4,5,6,7,8), partition.getMiddle());
        assertEquals(Arrays.asList(9,10,11,12), partition.getTail());
    }

    @Test
    public void getPartitionEmptyMiddle() throws Exception {
        Partitioner.Partition<Integer> partition = new Partitioner.Partition<>(
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12), 6, 6
        );

        assertEquals(Arrays.asList(1,2,3,4,5,6), partition.getHead());
        assertEquals(new ArrayList<Integer>(), partition.getMiddle());
        assertEquals(Arrays.asList(7,8,9,10,11,12), partition.getTail());
    }

}