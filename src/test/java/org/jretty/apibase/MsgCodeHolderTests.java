package org.jretty.apibase;

import java.util.Map;

public class MsgCodeHolderTests {

    public static void main(String[] args) {
        StringBuilder sbu = new StringBuilder();

        Map<String, Map<String, String>> msgsMap = MsgCodeHolder.getMsgsMap();
        for (String key : msgsMap.keySet()) {
            sbu.append("========================").append(key).append(" Code==========================\n");
            Map<String, String> msgs = msgsMap.get(key);
            for (String code : msgs.keySet()) {
                sbu.append(code).append(": ").append(msgs.get(code)).append("\n");
            }
        }

        System.out.println(sbu.toString());
    }

//    @SafeVarargs
//    public static Map<String, Map<String, String>> getCustomEnumValues(Class<? extends Enum<?>> ...enumClasses) {
//        Map<String, Map<String, String>> msgsMap = getMsgBaseEnumValues();
//        for(Class<? extends Enum<?>> enumClass: enumClasses) {
//            Enum<?>[] enums = enumClass.getEnumConstants();
//            Map<String, String> msgs = new LinkedHashMap<>();
//            for (Enum<?> e : enums) {
//                IMsg msg = (IMsg) e;
//                msgs.put(msg.getCode(), msg.getMessage());
//                // sbu.append(msg.getCode()).append(": ").append(msg.getMessage()).append("\n");
//            }
//            msgsMap.put(enumClass.getSimpleName(), msgs);
//        }
//        return msgsMap;
//    }

//    public static Map<String, Map<String, String>> getMsgBaseEnumValues() {
//        Map<String, String> msgs = new LinkedHashMap<>();
//        List<MsgBase> msgBase = MsgBase.getAllEnum();
//        for (MsgBase msg : msgBase) {
//            msgs.put(msg.getCode(), msg.getMessage());
//        }
//        return UT.newMap("MsgBase", msgs);
//    }

}
