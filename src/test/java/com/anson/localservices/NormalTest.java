package com.anson.localservices;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalTest {

    @Test
    void testBitMap() {
        int i = 0b1110111111111111111111111;

        long t1 = System.nanoTime();
        int count = 0;
        while (true){
            if ((i & 1) == 0){
                    break;
            }else{
                count++;
            }
            i >>>= 1;
        }
        long t2 = System.nanoTime();
        assertEquals(21, count);

        i = 0b1110111111111111111111111;
        long t3 = System.nanoTime();
        int count2 = 0;
        while (true) {
            if(i >>> 1 << 1 == i){

                break;
            }else{

                count2++;
            }

            i >>>= 1;
        }
        long t4 = System.nanoTime();
        assertEquals(21, count2);
    }
}
