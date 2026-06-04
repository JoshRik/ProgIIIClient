package it.unito.progiii.progiiiclient.utils;

public class AddressUtils {

    public static boolean checkAddressValid(String address) {
        return  address.matches(Constants.EMAIL_REGEX);
    }

    public static boolean checkReceiversValid(String receivers) {
        for (String receiver:receivers.split(" ")) {
            if(!checkAddressValid(receiver))
                return false;
        }
        return true;
    }

}
