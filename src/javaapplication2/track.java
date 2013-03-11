package javaapplication2;

/*
 * Модуль хранения путей
 */

/**
 *
 * @author alchemist
 */
public class track {
    /*
     * класс для хранения путей между городами(рёбер графа)
     */
    int Begin,End; //номера городов,которые связаны этим ребром
    int XBegin, YBegin, XEnd, YEnd;  //координаты этих городв
    
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
