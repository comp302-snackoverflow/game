package tr.edu.ku.comp302.domain.spells;

import tr.edu.ku.comp302.domain.entity.Lance;

public class Spell{

    public static void extendLance(Lance lance){
        System.out.println(lance.getLength());
        lance.setL(lance.getL() * 2);
        System.out.println(lance.getLength());
    }


}