package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setup(){
        SUT = new IntervalsAdjacencyDetector();
    }

    @Test
    public void isAdjacent_intervalOneBeforeIntervalTwo_falseReturned(){
        Interval intervalOne = new Interval(-1, 4);
        Interval intervalTwo = new Interval(5, 8);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_intervalOneBeforeAndAdjacentIntervalTwo_trueReturned(){
        Interval intervalOne = new Interval(-1, 5);
        Interval intervalTwo = new Interval(5, 8);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_intervalOneOverlapsIntervalTwoAtStart_falseReturned(){
        Interval intervalOne = new Interval(-1, 5);
        Interval intervalTwo = new Interval(3, 8);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_intervalOneContainedWithinIntervalTwo_falseReturned(){
        Interval intervalOne = new Interval(3, 5);
        Interval intervalTwo = new Interval(2, 8);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_intervalOneContainsIntervalTwo_falseReturned(){
        Interval intervalOne = new Interval(-1, 8);
        Interval intervalTwo = new Interval(2, 6);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_intervalOneEqualsIntervalTwo_trueReturned(){
        Interval intervalOne = new Interval(-1, 8);
        Interval intervalTwo = new Interval(-1, 8);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_intervalOneOverlapsIntervalTwoAtEnd_falseReturned(){
        Interval intervalOne = new Interval(-1, 5);
        Interval intervalTwo = new Interval(2, 8);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_intervalOneAfterAndAdjacentIntervalTwo_trueReturned(){
        Interval intervalOne = new Interval(5, 8);
        Interval intervalTwo = new Interval(2, 5);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_intervalOneAfterIntervalTwo_falseReturned(){
        Interval intervalOne = new Interval(5, 8);
        Interval intervalTwo = new Interval(2, 4);
        Boolean result = SUT.isAdjacent(intervalOne, intervalTwo);
        assertThat(result, is(false));
    }
    //Interval1 after and adjacent Interval2
    //Interval1 after interval2

}