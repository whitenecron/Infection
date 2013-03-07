package javaapplication2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alchemist
 */
public class track {
    int XBegin, YBegin, XEnd, YEnd,Begin,End;
    track(int begin, int end, int xbegin, int ybegin, int xend, int yend) {
        Begin=begin;
        End=end;
        XBegin=xbegin;
        XEnd=xend;
        YBegin=ybegin;
        YEnd=yend;
    }
    int getXBegin(){
        return XBegin;
    }
    int getXEnd(){
        return XEnd;
    }
    int getYBegin(){
        return YBegin;
    }
    int getYEnd(){
        return YEnd;
    }
    int getBegin(){
        return Begin;
    }
    int getEnd(){
        return End;
    }
}
