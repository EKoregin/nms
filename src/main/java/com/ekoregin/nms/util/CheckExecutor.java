package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;

public interface CheckExecutor {

    CheckResult checkExecute(Check check, Customer customer, Device device);

    default Device getDeviceForCheck(ModelDeviceService modelDeviceService, Check check, Customer customer){
        ModelDevice modelDevice = modelDeviceService.findById(check.getModelDevice().getId());
        return customer.getDevices().stream()
                .filter(device -> device.getModel().equals(modelDevice))
                .findFirst().orElse(null);
    }

    default Map<String, String> getParamsForCheck(Check check, Customer customer) {
        Map<String, String> paramsForCheck = new HashMap<>();
        for (TypeTechParameter type : check.getTypeTechParams()) {
            String paramValue = customer.getParams().stream()
                    .filter(parameter -> parameter.getType().equals(type))
                    .map(TechParameter::getValue)
                    .findFirst().orElse(Strings.EMPTY);
            paramsForCheck.put(type.getName(), paramValue);
        }
        return paramsForCheck;
    }

    default String replacingVariablesWithValues(String source, Map<String, String> params, Customer customer) {
        String CUSTOMER_NAME = "[#CUSTOMER_NAME]";
        String resultString = source;
        if (resultString.contains(CUSTOMER_NAME)) {
            resultString = resultString.replace(CUSTOMER_NAME, cyrillicToLatin(customer.getName()));
        }
        for (String param : params.keySet()) {
            resultString = resultString.replace("[#" + param + "]", params.get(param));
        }
        return resultString;
    }

    private String cyrillicToLatin(String message) {
        char[] abcCyr =   {' ','а','б','в','г','д','е','ж','з','и','й','к','л','м','н','о','п','р','с','т',
                'у', 'ф','х','ц','ч','ш','щ','э','ю','я','А','Б','В','Г','Д','Е', 'Ж','З','И','К','Л','М','Н', 'О',
                'П','Р','С','Т','У','Ф', 'Х','Ц','Ч','Ш','Щ','a','b','c','d','e','f','g','h','i','j','k','l','m',
                'n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N',
                'O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','/','-'};
        String[] abcLat = {" ","a","b","v","g","d","e","zh","z","i","y","k","l","m","n","o","p","r","s","t",
                "u","f","h","c","ch","sh","sh","e","u","ya","A","B","V","G","D","E","Zh","Z","I","K","L","M","N","O",
                "P","R","S","T","U","F","H","C","CH","Sh","sh","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o",
                "p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q",
                "R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","/","-"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }
}
