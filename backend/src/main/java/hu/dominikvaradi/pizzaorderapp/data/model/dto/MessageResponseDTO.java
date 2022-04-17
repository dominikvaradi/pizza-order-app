package hu.dominikvaradi.pizzaorderapp.data.model.dto;

import java.io.Serializable;

public class MessageResponseDTO implements Serializable {
    private String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
