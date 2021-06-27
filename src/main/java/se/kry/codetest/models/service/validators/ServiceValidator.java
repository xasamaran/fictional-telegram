package se.kry.codetest.models.service.validators;

import se.kry.codetest.models.service.Service;

public class ServiceValidator {

    public ServiceValidator(){
    }

    public void validate(Service service) {
        String urlRegex = "(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{2,3}.?([a-z]+)?";
        if(!service.getUrl().matches(urlRegex)){
            throw new IllegalArgumentException();
        }
    }
}



