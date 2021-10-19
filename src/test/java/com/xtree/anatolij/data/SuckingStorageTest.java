package com.xtree.anatolij.data;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SuckingStorageTest {
    @Test
    public void oneSuckingIsNotSuspicious() throws Exception {
        SuckingStorage.noteSucking("a",0);
        assertThat(SuckingStorage.isSuspicious("a"), is(false));
    }

    @Test
    public void twoSuckingIsNotSuspicious() throws Exception {
        SuckingStorage.noteSucking("b",0);
        SuckingStorage.noteSucking("b",2);
        assertThat(SuckingStorage.isSuspicious("b"), is(false));
    }

    @Test
    public void threeSuckingIsSuspicious() throws Exception {
        SuckingStorage.noteSucking("c",0);
        SuckingStorage.noteSucking("c",2);
        SuckingStorage.noteSucking("c",2);
        assertThat(SuckingStorage.isSuspicious("c"), is(true));
    }

    @Test
    public void fourthSuckingCleansBuffer() throws Exception {
        SuckingStorage.noteSucking("d",0);
        SuckingStorage.noteSucking("d",2);
        SuckingStorage.noteSucking("d",2);
        SuckingStorage.noteSucking("d",2000000);
        assertThat(SuckingStorage.isSuspicious("d"), is(false));
    }


}