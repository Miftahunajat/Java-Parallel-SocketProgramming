package com.util;


import com.bayudwiyansatria.io.IO;
import com.bayudwiyansatria.math.Math;

public class Core {
    private static Math math;
    private static IO io;

    public static Math getMatInstance(){
        if (math == null) math = new Math();
        return math;
    }

    public static IO getIoInstance(){
        if (io == null) io = new IO();
        return io;
    }
}
